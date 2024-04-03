import React, { useEffect, useState } from "react";

import { 
  Form, 
  Input, 
  List, 
  message,
  Select
} from 'antd';

const AddTransaction = (props) => {
    const [messageApi, contextHolder] = message.useMessage();
    const [users, setUsers] = useState([]);
    const [listUserName, setListUserName] = useState([]);

    const [selectedFile, setSelectedFile] = useState();
	const [userIDSelected, setUserIDSelected] = useState("");
	const [userNameSelected, setUserNameSelected] = useState("");
	const [senderUserId, setSenderUserId] = useState("");
	const [recipientUserId, setRecipientUserId] = useState("");
	const [transactionType, setTransactionType] = useState("");
	const [amount, setAmount] = useState("");

    const onAudioChange = (props) => {
        var file = document.getElementById('audio').files[0]
        setSelectedFile(file);
		console.log(file);
        var reader = new FileReader();
        reader.readAsDataURL(file)
    }

    const onSubmit = (event) => {
		//event.preventDefault();
		const formData = new FormData();
        var transactionData = {
            userID: userIDSelected[0],
			senderUserID: senderUserId,
			recipientUserID: recipientUserId,
			transactionType: transactionType,
			amount: amount
        }
        formData.append('file', selectedFile);
        formData.append('transaction', JSON.stringify(transactionData));
        console.log(transactionData)
        try{
            fetch('http://localhost:8080/api/v1/transactions/insert_transaction',{
                method: 'POST',
				//   headers:{
                //     "Content-type": "multipart/form-data",
                // },
                body: formData
            })
            .then(res => {
                if(res.status == 200) {
                    console.log('submit successfully', res)
                    props.setOpen(false);
                    props.success("Add successfully");
                    props.setIsAdded(true);
                }
                else {
                    props.danger(userNameSelected + " don't have privilege !");
                    props.setOpen(false);
                    console.log("asdasdas");
                }
            })
        }catch{
            console.log("err")
        }
	}

    const getUsersName = () => {
        fetch('http://localhost:8080/api/v1/users/TableUser', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setUsers(data[0]);
        });
    }

    const changeToValueArray = () => {
        var list = [];
        users?.map((item) => {
            list.push({
                value: item.userID,
                label: item.userName
            })
        })
        setListUserName(list);
    }

    useEffect(() => {
        getUsersName();
    }, [])

    useEffect(() => {
        changeToValueArray();
    }, [users])

    return (
        <div>
            <Form.Item
              name="title1"
              label="Username"
              rules={[
                {
                  required: true,
                  message: 'Please input username !',
                },
              ]}
            >
            <Select
                mode="multiple"
                style={{
                    flex: 1,
                    width: '100%'
                }}
                maxCount={1}
                onChange={(selected) => {
                    setUserIDSelected(selected);
                    setUserNameSelected(users.find(val => val.userID == selected).userName)
                }}
                options={listUserName}
            />
            </Form.Item>

            <Form.Item
              name="title2"
              label="Sender UserId"
            >
            <Input
                value={senderUserId}
                placeholder="Session Per User"
                onChange={ev => setSenderUserId(ev.target.value)}
                type="text" />
            </Form.Item>
            
            <Form.Item
              name="title3"
              label="Recipient UserId"
            >
            <Input
                value={recipientUserId}
                placeholder="Recipient UserId"
                onChange={ev => setRecipientUserId(ev.target.value)}
                type="text" />
            </Form.Item>
            
            <Form.Item
              name="title4"
              label="Transaction Type"
            >
            <Input
                value={transactionType}
                placeholder="Transaction Type"
                onChange={ev => setTransactionType(ev.target.value)}
                type="text" />
            </Form.Item>

            <Form.Item
              name="title5"
              label="Amount"
            >
            <Input
                value={amount}
                placeholder="Amount"
                onChange={ev => setAmount(ev.target.value)}
                type="number" />
            </Form.Item>
            
            <Form.Item
                name="title6"
                label="Voice"
            >
                <div>
					<input type="file" id="audio" onChange={(e) => onAudioChange()}></input>
				</div>
            </Form.Item>
            {contextHolder}
            <button 
                className="btn btn-primary w-100"
                onClick={(e) => {
                    onSubmit();
                }}    >
                    Add
            </button>
        </div>
    );
};

export default AddTransaction;