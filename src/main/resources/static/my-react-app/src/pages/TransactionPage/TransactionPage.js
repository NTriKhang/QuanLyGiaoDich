import React, { useState, useEffect } from 'react';
import './TransactionPage.css';
import Navbar from '../../components/navbar/Navbar.js';

const TransactionPage = () => {
	const [transactions, setTransactions] = useState([]);
	const [userName, setUserName] = useState('');
	const [error, setError] = useState(null);
	const [originalTransactions, setOriginalTransactions] = useState([]);
	const [searchTerm, setSearchTerm] = useState('');
	const [searchDate, setSearchDate] = useState('');

	useEffect(() => {
		let currentUser = localStorage.getItem('userNameKey');
		if (!currentUser) {
			setError('User not found.');
			return;
		}
		setUserName(currentUser);
		fetchTransactions(currentUser);
		console.log(currentUser);
	}, []);

	const fetchTransactions = async (currentUser) => {
		try {
			const response = await fetch(`http://localhost:8080/api/v1/transactions/userTransactions/${currentUser}`, {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				},
			});
			if (!response.ok) {
				window.alert("You don't have privilege.")
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const data = await response.json();
			setTransactions(data);
			setOriginalTransactions(data);
		} catch (error) {
			console.error('Error fetching transactions:', error);
			setError(error.message);
		}
	};

	const handleSearch = () => {
		let filteredTransactions = originalTransactions;

		if (searchTerm) {
			filteredTransactions = filteredTransactions.filter(transaction =>
				transaction.recipientUser?.toLowerCase().includes(searchTerm.toLowerCase()) ||
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

	const handleDeleteTransaction = async (transactionId) => {
		try {
			const confirmDelete = window.confirm("Are you sure you want to delete this transaction?");
			if (!confirmDelete) return;
			const response = await fetch(`http://localhost:8080/api/v1/transactions/${transactionId}`, {
				method: 'DELETE',
				headers: {
					'Content-Type': 'application/json',
					'UserName': userName
				}
			});
			if (response.ok) {
				const updatedTransactions = transactions.filter(transaction => transaction.transactionID !== transactionId);
				setTransactions(updatedTransactions);
				window.alert("Transaction deleted successfully.")
			} else {
				window.alert("You don't have privilege.")
				console.error('Error deleting transaction:', response.statusText);
			}
		} catch (error) {
			console.error('Error deleting transaction:', error);
		}
	};
	const handleEditTransaction = async (transactionId) => {
		try {
			const newAmount = parseFloat(window.prompt("Enter new amount:"));
			const newTransactionType = window.prompt("Enter new transaction type:");
			const response = await fetch(`http://localhost:8080/api/v1/transactions/${transactionId}`, {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/json',
					'UserName': userName
				},
				body: JSON.stringify({
					newAmount: newAmount,
					newTransactionType: newTransactionType
				})
			});
			if (response.ok) {
				const updatedTransactions = transactions.map(transaction => {
					if (transaction.transactionID === transactionId) {
						return {
							...transaction,
							amount: newAmount,
							transactionType: newTransactionType
						};
					}
					return transaction;
				});
				setTransactions(updatedTransactions);
				window.alert("Transaction updated successfully.")
			} else {
				window.alert("You don't have privilege.")
				console.error('Error updating transaction:', response.statusText);
			}
		} catch (error) {
			console.error('Error updating transaction:', error);
		}
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
			{(
				transactions.length > 0 ? (
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
									<td><button onClick={() => handleEditTransaction(transaction.transactionID)}>Sửa</button></td>
									<td><button onClick={() => handleDeleteTransaction(transaction.transactionID)}>Xóa</button></td>
								</tr>
							))}
						</tbody>
					</table>
				) : (
					<p>No transactions found for the current user.</p>
				)
			)}
		</div>
	);
}

export default TransactionPage;
