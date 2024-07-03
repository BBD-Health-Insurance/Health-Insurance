import React, { useState, useEffect } from 'react';
import './css/ClaimHistory.css';

export const ClaimHistory = () => {
  const [claimHistoryData, setClaimHistoryData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sortConfig, setSortConfig] = useState({ key: null, direction: 'ascending' });

  useEffect(() => {
    fetch(`${import.meta.env.VITE_APP_API_URL}/list-claimhistory`)
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then((data) => {
        setClaimHistoryData(data.claimHistories);
        setLoading(false);
      })
      .catch((error) => {
        setError(error);
        setLoading(false);
      });
  }, []);

  const requestSort = (key) => {
    let direction = 'ascending';
    if (sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });
  };

  const getClassNamesFor = (name) => {
    if (!sortConfig.key) {
      return;
    }
    return sortConfig.key === name ? sortConfig.direction : undefined;
  };

  const sortedData = [...claimHistoryData].sort((a, b) => {
    if (a[sortConfig.key] < b[sortConfig.key]) {
      return sortConfig.direction === 'ascending' ? -1 : 1;
    }
    if (a[sortConfig.key] > b[sortConfig.key]) {
      return sortConfig.direction === 'ascending' ? 1 : -1;
    }
    return 0;
  });

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div className="claim-history-container">
      <h1>Claim History</h1>
      <table className="claim-history-table">
        <thead>
          <tr>
            <th onClick={() => requestSort('claimHistoryID')} className={getClassNamesFor('claimHistoryID')}>
              Claim History ID
            </th>
            <th onClick={() => requestSort('coverPlan.coverPlanID')} className={getClassNamesFor('coverPlan.coverPlanID')}>
              Cover Plan ID
            </th>
            <th onClick={() => requestSort('claimAmount')} className={getClassNamesFor('claimAmount')}>
              Claim Amount
            </th>
            <th onClick={() => requestSort('amountPaid')} className={getClassNamesFor('amountPaid')}>
              Amount Paid
            </th>
            <th onClick={() => requestSort('claimPersonaID')} className={getClassNamesFor('claimPersonaID')}>
              Claim Persona ID
            </th>
            <th onClick={() => requestSort('timeStamp')} className={getClassNamesFor('timeStamp')}>
              Time Stamp
            </th>
          </tr>
        </thead>
        <tbody>
          {sortedData.map((claim) => (
            <tr key={claim.claimHistoryID}>
              <td>{claim.claimHistoryID}</td>
              <td>{claim.coverPlan.coverPlanID}</td>
              <td>{claim.claimAmount}</td>
              <td>{claim.amountPaid}</td>
              <td>{claim.claimPersonaID}</td>
              <td>{new Date(claim.timeStamp).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
