import React, { useEffect, useState } from "react";
import './AuditWarning.css';
import { useNavigate } from "react-router-dom"; 
import Navbar from "../../components/navbar/Navbar";

const AuditWarning = () => {
	const [alerts, setAlerts] = useState([]);
	const navigate = useNavigate();
	const [originalAlerts, setOriginalAlerts] = useState([]);
	const [searchTerm, setSearchTerm] = useState('');
	const [searchDate, setSearchDate] = useState('');

	useEffect(() => {
		let username = localStorage.getItem('userNameKey');
		if (username) {
			username = username.split(" ")[0];
			fetch(`http://localhost:8080/api/v1/alerts/alerts/${username}`)
				.then(response => {
					if (response.status === 204) {
						return [];
					} else if (!response.ok) {
						throw new Error('Network response was not ok');
					}
					return response.json();
				})
				.then(data => {
					setOriginalAlerts(data);
					setAlerts(data);
				})
				.catch(error => {
					console.error('Error fetching data:', error);
				});
		}
	}, []);
	const handleSearch = () => {
		
		let filteredAlerts = originalAlerts;
		if (searchTerm) {
			filteredAlerts = filteredAlerts.filter(alert =>
				alert.recipientName.toLowerCase().includes(searchTerm.toLowerCase())
			);
		}
		if (searchDate) {
			filteredAlerts = filteredAlerts.filter(alert => {
				const alertDate = new Date(alert.createdDate).toISOString().split('T')[0];
				return searchDate === alertDate;
			});
		}
		setAlerts(filteredAlerts);
	};

	return (
		<div className="audit-warning">
			<Navbar />
			<div>
				<button
					className="btn btn-primary me-2"
					onClick={() => navigate("/addAudit")}>Create new</button>
				<button
					className="btn btn-primary me-2"
					onClick={() => navigate("/auditManage")}>Show audit</button>
				<button
					className="btn btn-primary me-2"
					onClick={() => navigate("/auditWarning")}>Show warning</button>
			</div>
			<div className="search-bar">
				<input
					type="text"
					placeholder="Search by name..."
					value={searchTerm}
					className="search-input"
					onChange={(e) => setSearchTerm(e.target.value)}
				/>
				<input
					type="date"
					value={searchDate}
					className="search-date"
					onChange={(e) => setSearchDate(e.target.value)}
				/>
				<button className="search-button" onClick={handleSearch}>Search</button>
			</div>
			<h2>Audit Warning</h2>
			{alerts.length > 0 ? (
				<ul className="alert-list">
					{alerts.map((alert, index) => (
						<li key={index} className="alert-item">
							<p>Message: {alert.message}</p>
							<p>Date: {new Date(alert.createdDate).toLocaleString()}</p>
						</li>
					))}
				</ul>
			) : (
				<p>No warning found</p>
			)}
		</div>
	);
};

export default AuditWarning;
