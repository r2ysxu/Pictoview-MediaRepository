import React from 'react';
import LoginForm from '../login';
import './styles.css';

function Home(props) {

  const onLogin = () => {
    window.location.replace('/album');
  }

  return (
    <div>
      <div className="home_banner_container">
        <h2>Media Repository Network</h2>
      </div>
      <LoginForm onLoggedIn={onLogin} />
    </div>
  );
}

export default Home