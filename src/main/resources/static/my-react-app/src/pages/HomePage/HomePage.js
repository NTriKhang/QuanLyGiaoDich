import React, { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom";
import "./HomePage.css"
import Navbar from "../../components/navbar/Navbar.js";

const Home = (props) => {
    const [userRecipentName, setUsername] = useState('');
    const [money, setMoney] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        // Perform transaction handling here
        console.log('Username:', userRecipentName);
        console.log('Money:', money);

        var transaction = {
          userName: localStorage.getItem('userNameKey'),
          recipentUserName: userRecipentName,
          amount: money
        }
        console.log(transaction)
        try{
          fetch('http://localhost:8080/api/v1/transactions',{
              method: 'POST',
              // mode: 'no-cors',
              // headers:{
              //     "Content-type": "multipart/form-data",
              // },
              body: JSON.stringify(transaction)
          }).then(res => {
              console.log(res)
              if(res.status == 201){
                  alert("Tranfer success")
              }
              else{
                  alert(res.body)
              }

          });
      }catch{
          console.log("err")
      }
        // Reset form fields
        setUsername('');
        setMoney('');
    };

    useEffect(() => {
        if (!localStorage.getItem("userNameKey")) {
            navigate("/signin");
        }
      }, []);

    return (
       <div>
            <Navbar />
            <h2>Transaction Form</h2>
      <form onSubmit={handleSubmit} className="center_form">
        <div>
          <label>Username:</label>
          <input
            type="text"
            value={userRecipentName}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Enter username"
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
    )
}

export default Home