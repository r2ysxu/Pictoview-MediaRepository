import React from 'react';
import { Link } from "react-router-dom";
import { useState } from 'react';
import './Login.css';

const emptyUser = {username: '', password: ''};

const convertObjectToURLEncoded = (formData) => {
	let formBody = [];
	for (let property in formData) {
	  formBody.push(encodeURIComponent(property) + "=" + encodeURIComponent(formData[property]));
	}
	return formBody.join("&");
}

function LoginForm({onLoggedIn}) {
	const [user, setUser] = useState(emptyUser);
	const [loginErrors, setLoginErrors] = useState(false);

	const handleSubmit = (event) => {
		event.preventDefault();
		fetch('/login', {
			method: 'POST',
			body: convertObjectToURLEncoded(user),
			headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
		}).then( response => {
			setUser(emptyUser);
			if (response.status === 200) {
				onLoggedIn();
				window.location.reload();
			} else if (response.status === 201) {
				setLoginErrors(true);
			}
		});
	}

	return (
		<div className="login_form_container login_form_float_out">
			<div className="login_form_error">
				{ loginErrors ? <span>Invalid Username/password</span> : <span/>}
			</div>
			<form name="loginForm" onSubmit={handleSubmit}>
		        <div>
		        	<input type="text" placeholder="Username"
		        		 name="username" value={user.username}
		        		 onChange={e => setUser({ ...user, username: e.target.value})} />
		        	<input type="password" placeholder="Password"
		        		name="password" value={user.password}
		        		onChange={e => setUser({ ...user, password: e.target.value})} />
		        	<button className="login_form_button" type="submit">Log in</button>
		        </div>
			</form>
			<div className="login_form_additional_login_options">
				<Link to="">Forgot Password?</Link>
			</div>
		</div>
	);
}

export default LoginForm;