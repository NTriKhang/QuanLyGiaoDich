import React, { useState, useEffect } from 'react';
import './TransactionPage.css';
import Navbar from '../../components/navbar/Navbar.js';

const TransactionList = () => {
	const [transactions, setTransactions] = useState([]);
	const [loading, setLoading] = useState(false);
	const [userName, setUserName] = useState('');
	const [error, setError] = useState(null);
	const [originalTransaction, setOriginalTransaction] = useState([]);
	const [searchTerm, setSearchTerm] = useState('');
	const [searchDate, setSearchDate] = useState('');

	useEffect(() => {
		let currentUser = localStorage.getItem('userNameKey');
		if (!currentUser) {
			setError('User not found.');
			return;
		}
		currentUser = currentUser.split(" ")[0];
		setUserName(currentUser);
		setLoading(true);
		fetch('http://localhost:8080/api/v1/transactions/user-transaction?username=' + currentUser)
			.then(response => {
				if (!response.ok) {
					throw new Error(`HTTP error! status: ${response.status}`);
				}
				return response.json();
			})
			.then(data => {
				setTransactions(data);
				setOriginalTransaction(data);
				setLoading(false);
			})
			.catch(error => {
				console.error('Error fetching transactions:', error);
				setError(error.message);
				setLoading(false);
			});
	}, [])

	const handleSearch = () => {
		let filteredTransactions = originalTransaction;

		if (searchTerm) {
			filteredTransactions = filteredTransactions.filter(transaction =>
				transaction.recipientName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
				transaction.senderUser?.toLowerCase().includes(searchTerm.toLowerCase())
			);
		}
		if (searchDate) {
			const searchDateFormatted = new Date(searchDate).toLocaleDateString('vi-VN');
			filteredTransactions = filteredTransactions.filter(transaction => {
				if (!transaction.transactionDate) {
					return false;
				}
				const transactionDateFormatted = new Date(transaction.transactionDate).toLocaleDateString('vi-VN');
				return searchDateFormatted === transactionDateFormatted;
			});
		}
		setTransactions(filteredTransactions);
	};


	return (
		<div>
			<Navbar />
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
			<h1>Transactions of {userName}</h1>
			{transactions.length > 0 ? (
				<table>
					<thead>
						<tr>
							<th>Transaction ID</th>
							<th>Sender User</th>
							<th>Recipient User</th>
							<th>Transaction Type</th>
							<th>Amount</th>
							<th>Transaction Date</th>
						</tr>
					</thead>
					<tbody>
						{transactions.map(transaction => (
							<tr key={transaction.transactionID}>
								<td>{transaction.transactionID}</td>
								<td>{transaction.senderUser}</td>
								<td>{transaction.recipientUser}</td>
								<td>{transaction.transactionType}</td>
								<td>{transaction.amount}</td>
								<td>{new Date(transaction.transactionDate).toLocaleDateString('vi-VN')}</td>
							</tr>
						))}
					</tbody>
				</table>
			) : (
				<p>No transactions found for the current user.</p>
			)}
		</div>
	);
}

export default TransactionList;
