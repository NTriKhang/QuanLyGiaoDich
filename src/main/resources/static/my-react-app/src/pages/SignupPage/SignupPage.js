import React, { useState } from "react";
import "./SignupPage.css"
import { json, useNavigate } from "react-router-dom";

import '@fortawesome/fontawesome-free/css/all.min.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faUser, 
    faLock, 
    faPhone, 
    faMapLocation, 
    faEnvelope,
} from '@fortawesome/free-solid-svg-icons';
import Banner from "../../components/banner/Banner"

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
                // mode: 'no-cors',
                // headers:{
                //     "Content-type": "multipart/form-data",
                // },
                body: formData
            }).then(res => {
                console.log('submit successfully', res)
                navigate("/signin")
            });
        }catch{
            console.log("err")
        }
        
    }
    const first_state = () => {
        return(
            <div className={"mainContainer row"}>
                <Banner />
                <div className={"col-6"}>
                    <div className="form-container d-flex flex-column justify-content-end align-items-center">
                        <form className={"w-100"} onSubmit={onNextClick}>
                        <div className={"titleContainer"}>
                            <div>Online Banking</div>
                        </div>
                        <br />
                        <div className="input_contain">
                            <div className={"container-input-form me-3 w-100"}>
                                <FontAwesomeIcon 
                                    icon={faUser}
                                    className="icon-input" />
                                <input
                                    value={firstname}
                                    placeholder="First name"
                                    type="text"
                                    onChange={ev => setFirstname(ev.target.value)}
                                    className={"inputBox form-control pt-4 pb-4"} />
                            </div>
                            <div className={"container-input-form w-100"}>
                            <FontAwesomeIcon 
                                    icon={faUser}
                                    className="icon-input" />
                                <input
                                    value={lastname}
                                    placeholder="Last name"
                                    type="text"
                                    onChange={ev => setLastname(ev.target.value)}
                                    className={"inputBox form-control"} />
                            </div>
                        </div>
                        <div className="input_contain">
                            <div className={"container-input-form me-3 mt-3 w-100"}>
                            <FontAwesomeIcon 
                                    icon={faPhone}
                                    className="icon-input" />
                                <input
                                    value={phone}
                                    placeholder="Phone"
                                    type="number"
                                    onChange={ev => setPhone(ev.target.value)}
                                    className={"inputBox form-control"} />
                            </div>
                            <div className={"container-input-form mt-3 w-100"}>
                            <FontAwesomeIcon 
                                    icon={faEnvelope}
                                    className="icon-input" />
                                <input
                                    value={email}
                                    placeholder="Email"
                                    type="email"
                                    onChange={ev => setEmail(ev.target.value)}
                                    className={"inputBox form-control"} />
                            </div>
                        </div>
                        <div className="input_contain">
                            <div className={"container-input-form mt-3 w-100"}>
                            <FontAwesomeIcon 
                                    icon={faMapLocation}
                                    className="icon-input" />
                                <input
                                    value={address}
                                    placeholder="Address"
                                    type="text"
                                    onChange={ev => setAddress(ev.target.value)}
                                    className={"inputBox form-control"} />
                            </div>
                        </div>
                        <br />
                            <div className={""}>
                                <button className="btn-custom w-100 m-0 mb-3" type="submit">Next</button>
                            </div>
                            <div className={""}>
                                <input
                                    className={"inputButton w-100 m-0"}
                                    type="button"
                                    onClick={onBackClick}
                                    value={"Back"} />
                        </div>
                        </form>
                    </div>
                </div>
            </div>
        )
    }
    const second_state = () => {
        return (
            <div className="mainContainer row">
                <Banner />
                <div className="col-6">
                    <div className="form-container d-flex flex-column justify-content-end align-items-center">
                        <form className={""} onSubmit={onSubmit}>
                            <div className={"titleContainer"}>
                                <div>Sign up</div>
                            </div>
                            <br />
                            <div className="input_contain mb-4">
                                <div className={"container-input-form me-3 w-100"}>
                                    <FontAwesomeIcon 
                                        icon={faUser}
                                        className="icon-input" />
                                    <input
                                        value={username}
                                        placeholder="User name"
                                        onChange={ev => setUsername(ev.target.value)}
                                        className={"inputBox form-control"} />
                                </div>
                                <div className={"container-input-form w-100"}>
                                    <FontAwesomeIcon 
                                        icon={faLock}
                                        className="icon-input" />
                                    <input
                                        value={password}
                                        placeholder="Password"
                                        type="password"
                                        onChange={ev => setPassword(ev.target.value)}
                                        className={"inputBox form-control"} />
                                </div>
                            </div>
                            <div className="" style={{width:'50%', display:'inline-block', height:'150px'}}>
                                <button className="background-profile" 
                                        style={{border: '1px solid', borderRadius:'20px', height:'100%', width:'100%'}}
                                        onClick={onPictureClick} id="picture_display">
                                </button>
                                <input type="file" id="profile_picture" onChange={onPictureChange} style={{display:"none"}}></input>
                            </div>
                            <div className={""}>
                                <button className="btn-custom w-100 m-0 mt-3" type="submit" onClick={onSubmit}>Create</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
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
