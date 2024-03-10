import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';
import Navbar from '../../components/navbar/Navbar.js';

const Home = (props) => {
	const [userRecipientName, setUsername] = useState('');
	const [money, setMoney] = useState('');
	const [alertMessage, setAlertMessage] = useState('');
	const navigate = useNavigate();

	useEffect(() => {
		if (!localStorage.getItem('userNameKey')) {
			navigate('/signin');
		}
	}, [navigate]);

	const fetchLatestAlert = () => {
		fetch('http://localhost:8080/api/v1/alerts/latest')
			.then(response => response.json())
			.then(data => {
				if (data.message) {
					setAlertMessage(data.message);
				}
			})
			.catch(error => {
				console.error("Error:", error);
			});
	};

        

	const handleSubmit = async (e) => {
		e.preventDefault();

		const transaction = {
			userName: localStorage.getItem('userNameKey'),
			recipientUserName: userRecipientName,
			amount: parseFloat(money),
		};

		try {
			const response = await fetch('http://localhost:8080/api/v1/transactions', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify(transaction),
			});

			const data = await response.json();

			if (response.ok) {
				if (data.warning) {
					alert(data.warning);
					setAlertMessage(data.warning);
				} else {
					alert('Transfer successful');
					setAlertMessage('');
				}
			} else {
				alert(data.message || 'Failed to transfer.');
			}
		} catch (error) {
			console.error('Error:', error);
			alert('An error occurred while processing the transaction');
		}
		setUsername('');
		setMoney('');
	};



	return (
		<div>
			<Navbar />
			{alertMessage && <div className="alert">{alertMessage}</div>}
			<h2>Transaction Form</h2>
			<form onSubmit={handleSubmit} className="center_form">
				<div>
					<label>Username:</label>
					<input
						type="text"
						value={userRecipientName}
						onChange={(e) => setUsername(e.target.value)}
						placeholder="Enter recipient's username"
						required
					/>
				</div>
				<div>
					<label>Money:</label>
					<input
						type="number"
						value={money}
						onChange={(e) => setMoney(e.target.value)}
						placeholder="Enter money amount"
						required
					/>
				</div>
				<button type="submit">Submit</button>
			</form>
		</div>
	);
};

export default Home;
