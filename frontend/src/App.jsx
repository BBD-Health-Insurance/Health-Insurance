import React, { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Navbar } from './navBar';
import { ClaimHistory, Welcome, Home, Callback } from './components';

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
            <Route path="/claim-history" element={<ClaimHistory/>} />
            <Route path="/home" element={<Home/>} />
            <Route path="/callback" element={<Callback/>} />
            <Route path="/" element={<Welcome/>} />
          </Routes>
        </div>
      </div>
    </BrowserRouter>
  );
};

export default App;
