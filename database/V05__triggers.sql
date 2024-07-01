USE HealthInsurance;
GO

-- Drop the existing trigger if it exists
IF OBJECT_ID('trg_check_max_cover', 'TR') IS NOT NULL
    DROP TRIGGER trg_check_max_cover;
GO

-- Create the INSTEAD OF INSERT trigger
CREATE TRIGGER trg_check_max_cover
ON ClaimHistory
INSTEAD OF INSERT
AS
BEGIN
    -- Declare variables to hold the inserted values
    DECLARE @coverPlanID bigint;
    DECLARE @claimAmount DECIMAL(10, 2);
    DECLARE @amountPaid DECIMAL(10, 2);
    DECLARE @maxCover DECIMAL(10, 2);
    DECLARE @claimPersonaID bigint;

    -- Get the maxCover value from the MaxCover table (assuming there's only one row)
    SELECT @maxCover = maxCover
    FROM MaxCover;

    -- Cursor to process each row in inserted table
    DECLARE inserted_cursor CURSOR FOR
    SELECT coverPlanID, claimAmount, amountPaid, claimPersonaID
    FROM inserted;

    -- Initialize variables for fetching
    DECLARE @fetch_status INT;

    -- Open the cursor
    OPEN inserted_cursor;

    -- Fetch the first row
    FETCH NEXT FROM inserted_cursor INTO @coverPlanID, @claimAmount, @amountPaid, @claimPersonaID;

    -- Loop through each row
    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- Check if amountPaid exceeds maxCover
        IF @amountPaid > @maxCover
        BEGIN
			-- Raise an error and rollback the transaction
            RAISERROR ('The amountPaid exceeds the maximum cover limit.', 16, 1);

			FETCH NEXT FROM inserted_cursor INTO @coverPlanID, @claimAmount, @amountPaid, @claimPersonaID;
        END
		ELSE
		BEGIN

			-- Insert the row into ClaimHistory
			INSERT INTO ClaimHistory (coverPlanID, claimAmount, amountPaid, claimPersonaID)
			VALUES (@coverPlanID, @claimAmount, @amountPaid, @claimPersonaID);

			-- Fetch the next row
			FETCH NEXT FROM inserted_cursor INTO @coverPlanID, @claimAmount, @amountPaid, @claimPersonaID;
		END
    END;

    -- Close and deallocate the cursor
    CLOSE inserted_cursor;
    DEALLOCATE inserted_cursor;
END;
GO
