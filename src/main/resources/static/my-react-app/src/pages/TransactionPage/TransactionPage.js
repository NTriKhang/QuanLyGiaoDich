import React, { useState, useEffect } from 'react';
import './TransactionPage.css';
import Navbar from '../../components/navbar/Navbar.js';
import { Form, Modal, message } from 'antd';

import AddTransaction from './AddTransaction.js';

const TransactionPage = () => {
	const [messageApi, contextHolder] = message.useMessage();
	const [transactions, setTransactions] = useState([]);
	const [userName, setUserName] = useState('');
	const [error, setError] = useState(null);
	const [originalTransactions, setOriginalTransactions] = useState([]);
	const [searchTerm, setSearchTerm] = useState('');
	const [searchDate, setSearchDate] = useState('');
	const [open, setOpen] = useState(false);
    const [isAdded, setIsAdded] = useState(false);

	const success = (p_content) => {
		messageApi.open({
		  type: 'success',
		  content: p_content,
		});
	  };

	const danger = (p_content) => {
	messageApi.open({
		type: 'error',
		content: p_content,
	});
	};

	  const CollectionCreateForm = ({ initialValues, onFormInstanceReady }) => {
        const [form] = Form.useForm();
        useEffect(() => {
          onFormInstanceReady(form);
        }, []);
        return (
          <Form layout="vertical" form={form} name="form_in_modal">
            <AddTransaction setOpen={setOpen} success={success} setIsAdded={setIsAdded} danger={danger}/>
          </Form>
        );
      };
      const CollectionCreateFormModal = ({ open, onCancel, initialValues }) => {
        const [formInstance, setFormInstance] = useState();
        return (
          <Modal
            open={open}
            title="Add Transaction"
            okText="Create"
            cancelText="Cancel"
            okButtonProps={{
              autoFocus: true,
            }}
            footer={[]}
            onCancel={onCancel}
            destroyOnClose
            onOk={async () => {
              try {
                const values = await formInstance?.validateFields();
                formInstance?.resetFields();
              } catch (error) {
                console.log('Failed:', error);
              }
            }}
          >
            <CollectionCreateForm
              initialValues={initialValues}
              onFormInstanceReady={(instance) => {
                setFormInstance(instance);
              }}
            />
          </Modal>
        );
      };

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
				alert("You don't have privilege");
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const data = await response.json();
			console.log(data);
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
				window.alert("You don't have privilege");
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
				window.alert("You don't have privilege");
				console.error('Error updating transaction:', response.statusText);
			}
		} catch (error) {
			console.error('Error updating transaction:', error);
		}
	};

	return (
		<div>
			{contextHolder}
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
			<CollectionCreateFormModal
                    open={open}
                    onCancel={() => setOpen(false)}
                    initialValues={{
                    modifier: 'public',
                    }}
                />
			<button
				className='btn btn-primary d-flex mx-4' 
				onClick={() => setOpen(true)} >
					Add New
			</button>
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
								<th>Audio</th>
								<th colSpan={2}>Action</th>
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
									<td>
										<audio controls>
											<source src={`data:audio/mp3;base64,${transaction.voice}`} type="audio/mp3" />
											Audio
										</audio>
									 </td>
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
