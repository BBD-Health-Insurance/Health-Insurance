import React, { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Navbar } from './navBar';
import { PersonaPlan, ClaimHistory, StockShares, Welcome, Home, ProtectedRoute } from './components';

const App = () => {

  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const onSignIn = () => {
    setIsAuthenticated(true);
  }

  const onSignOut = () => {
    setIsAuthenticated(false);
  }


  return (
    <BrowserRouter>
      <div>

        <Navbar
          isAuthenticated={isAuthenticated}
          onSignIn={onSignIn}
          onSignOut={onSignOut}/>

        <div className="content">
          <Routes>
            <Route path="/persona-plan" element={<PersonaPlan/>} />
            <Route path="/claim-history" element={<ClaimHistory/>} />
            <Route path="/stock-shares" element={<StockShares/>} />
            <Route path="/home" element={<Home/>} />
            <Route path="/" element={<Welcome/>} />
          </Routes>
        </div>
      </div>
    </BrowserRouter>
  );
};

export default App;
