import React from 'react';
import { useState } from 'react';
import LoginForm from '../components/login/LoginForm';
import HomeBanner from '../components/home_banner/HomeBanner';

function Home(props) {
    const [loggedIn, setLoggedIn] = useState(false);

	return (
		<div>
		   <HomeBanner />
		</div>
	);
}

export default Home