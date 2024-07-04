import React from 'react';
import './css/Home.css';

export const Home = () => {
  return (
    <div className="home-container">

      <h1 className="home-heading">Welcome to the Health Insurance Admin Portal!</h1>
      <p className="home-subtext">
        Manage and oversee all health insurance claim histories seamlessly.
      </p>

      <div className="home-info-section">
        <div className="home-info-box">
          <h2 className="info-title">Latest Updates</h2>
          <p>Stay informed with the latest changes in health insurance in our mini-economy with millions of brilliant persona.</p>
        </div>
      </div>
    </div>
  );
};