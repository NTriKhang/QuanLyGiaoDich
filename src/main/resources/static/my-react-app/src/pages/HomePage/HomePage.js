import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';
import Navbar from '../../components/navbar/Navbar.js';

const Home = (props) => {
	const [userRecipientName, setUsername] = useState('');
	const [money, setMoney] = useState('');
	const [alertMessage, setAlertMessage] = useState('');
	const navigate = useNavigate();
	const [shouldFetchAlert, setShouldFetchAlert] = useState(false);
	const [selectedFile, setSelectedFile] = useState();

	const onAudioChange = () => {
        var file = document.getElementById('audio').files[0]
        setSelectedFile(file);
		console.log(file);
        var reader = new FileReader();
        reader.readAsDataURL(file)
    }

	useEffect(() => {
		if (!localStorage.getItem('userNameKey')) {
			navigate('/signin');
		}
	}, [navigate]);

	useEffect(() => {
		if (shouldFetchAlert) {
			fetchLatestAlert();
			setShouldFetchAlert(false); 
		}
	}, [shouldFetchAlert]);
	const fetchLatestAlert = async () => {
		try {
			const response = await fetch('http://localhost:8080/api/v1/alerts/latest-unprocessed');
			if (!response.ok) throw new Error('Failed to fetch latest alert.');

			const alertData = await response.json();
			if (alertData && alertData.message) {
				setAlertMessage(alertData.message);
				await markAlertAsProcessed(alertData.alertID);
			}
		} catch (error) {
			console.error("Error:", error);
		}
	};

	const markAlertAsProcessed = async (alertID) => {
		if (!alertID) return;

		try {
			await fetch(`http://localhost:8080/api/v1/alerts/mark-processed/${alertID}`, {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/json',
				},
			});
		} catch (error) {
			console.error("Error marking alert as processed:", error);
		}
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		// const transaction = {
		// 	userName: localStorage.getItem('userNameKey'),
		// 	recipientUserName: userRecipientName,
		// 	amount: parseFloat(money),
		// };
		
		try {
			const formData = new FormData();
			var transactionDto = {
				userName: localStorage.getItem('userNameKey'),
				recipientUserName: userRecipientName,
				amount: parseFloat(money)
			}
			formData.append('file', selectedFile);
			formData.append('transactionDto', JSON.stringify(transactionDto));
			console.log(transactionDto)
			const response = await fetch('http://localhost:8080/api/v1/transactions', {
				method: 'POST',
				body: formData, 
			});
		
			if (!response.ok) {
				const errorData = await response.json();
				window.alert('Failed to transfer.');
			}
			else{
				window.alert('Transfer successful');
			}
		} finally {
			setUsername('');
			setMoney('');
		}
	};
	
	
	return (
		<div>
			<Navbar />
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
				<div>
					<input type="file" id="audio" onChange={(e) => onAudioChange()}></input>
				</div>
				<button 
					className='my-4' 
					type="submit" >Submit</button>
			</form>
		</div>
	);
};

export default Home;
