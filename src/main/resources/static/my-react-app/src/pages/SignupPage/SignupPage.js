import React, { useState } from "react";
import "./SignupPage.css"
import { json, useNavigate } from "react-router-dom";

export default function Signup () {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const [firstname, setFirstname] = useState("")
    const [lastname, setLastname] = useState("")
    const [phone, setPhone] = useState("")
    const [email, setEmail] = useState("")
    const [address, setAddress] = useState("")
    const [selectedFile, setSelectedFile] = useState(null);
    const [formstate, setFormstate] = useState("first")
    
    const navigate = useNavigate();
        
    const onNextClick = (event) => {
        event.preventDefault();
        setFormstate("second")
        return false;
    }
    const onBackClick = () => {
        navigate('/signin')
    }
    const onPictureClick = (event) => {
        event.preventDefault();
        console.log("click")
        document.getElementById('profile_picture').click();
    }
    const onPictureChange = () => {
        var file = document.getElementById('profile_picture').files[0]
        setSelectedFile(file);
        var reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('picture_display').style.backgroundImage = "url('" + e.target.result + "')";
        }
        reader.readAsDataURL(file)
    }
    const onSubmit = async (event) => {
        event.preventDefault();
        const formData = new FormData();
        var userData = {
            firstName: firstname,
            lastName: lastname,
            address: address,
            phone: phone,
            email: email,
            userName: username,
            password: password
        }
        formData.append('file', selectedFile);
        formData.append('user', JSON.stringify(userData));
        console.log(userData)
        try{
            fetch('http://localhost:8080/api/v1/users',{
                method: 'POST',
                mode: 'no-cors',
                // headers:{
                //     "Content-type": "multipart/form-data",
                // },
                body: formData
            }).then(res => {
                console.log('submit successfully', res)
            });
        }catch{
            console.log("err")
        }
        
    }
    const first_state = () => {
        return(
            <form className={""} onSubmit={onNextClick}>
                <div className={"titleContainer"}>
                    <div>Sign up</div>
                </div>
                <br />
                <div className="input_contain">
                    <div className={"m-2"}>
                        <input
                            value={firstname}
                            placeholder="First name"
                            type="text"
                            onChange={ev => setFirstname(ev.target.value)}
                            className={"inputBox"} />
                    </div>
                    <div className={"m-2"}>
                        <input
                            value={lastname}
                            placeholder="Last name"
                            type="text"
                            onChange={ev => setLastname(ev.target.value)}
                            className={"inputBox"} />
                    </div>
                </div>
                <div className="input_contain">
                    <div className={"m-2"}>
                        <input
                            value={phone}
                            placeholder="Phone"
                            type="number"
                            onChange={ev => setPhone(ev.target.value)}
                            className={"inputBox"} />
                    </div>
                    <div className={"m-2"}>
                        <input
                            value={email}
                            placeholder="Email"
                            type="email"
                            onChange={ev => setEmail(ev.target.value)}
                            className={"inputBox"} />
                    </div>
                </div>
                <div className="input_contain">
                    <div className={"m-2"}>
                        <input
                            value={address}
                            placeholder="Address"
                            type="text"
                            onChange={ev => setAddress(ev.target.value)}
                            className={"inputBox"} />
                    </div>
                </div>
                <br />
                    <div className={""}>
                        <button className="btn" type="submit">Next</button>
                    </div>
                    <div className={""}>
                        <input
                            className={"inputButton"}
                            type="button"
                            onClick={onBackClick}
                            value={"Back"} />
                </div>
            </form>
        )
    }
    const second_state = () => {
        return (
            <form className={""} onSubmit={onSubmit}>
                <div className={"titleContainer"}>
                    <div>Sign up</div>
                </div>
                <br />
                <div className="input_contain">
                    <div className={"m-2"}>
                        <input
                            value={username}
                            placeholder="User name"
                            onChange={ev => setUsername(ev.target.value)}
                            className={"inputBox"} />
                    </div>
                    <div className={"m-2"}>
                        <input
                            value={password}
                            placeholder="Password"
                            type="password"
                            onChange={ev => setPassword(ev.target.value)}
                            className={"inputBox"} />
                    </div>
                </div>
                <div className="" style={{width:'20%', display:'inline-block', height:'220px'}}>
                    <button className="background-profile" 
                            style={{border: '1px solid', borderRadius:'20px', height:'100%', width:'100%'}}
                            onClick={onPictureClick} id="picture_display">
                    </button>
                    <input type="file" id="profile_picture" onChange={onPictureChange} style={{display:"none"}}></input>
                </div>
                <div className={""}>
                    <button className="btn" type="submit" onClick={onSubmit}>Create</button>
                </div>
            </form>
        )
    }
    let form;
    if(formstate === 'first'){
        form = first_state();
        console.log('first')
    }
    else if(formstate === 'second'){
        form = second_state();
        console.log('second')
    }


    return (
        <article>
            {form}
        </article>
    );
}
