import React, { useState } from "react"
import { useNavigate } from "react-router-dom";
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';

const NavbarComponent = () => {
    const [username, setUsername] = useState("");

    const onSignOutClick = () => {
        // You'll update this function later
        if(localStorage.getItem('userNameKey') != null){
            try{
                console.log('trye')
                fetch('http://localhost:8080/api/v1/users/logout/' + localStorage.getItem('userNameKey'),{
                    method: 'GET',
                    // mode: 'no-cors',
                }).then(res => {
                    console.log('submit successfully', res)
                    localStorage.removeItem("userNameKey")
                    alert('logout success')
                    navigate('/signIn')
                });
            }catch{
                console.log("err")
            }
        }

    }
    const onSignOutAllClick = () => {
        // You'll update this function later
        if(localStorage.getItem('userNameKey') != null){
            try{
                console.log('trye')
                fetch('http://localhost:8080/api/v1/users/logout_all/' + localStorage.getItem('userNameKey').split(' ')[0],{
                    method: 'GET',
                    // mode: 'no-cors',
                }).then(res => {
                    console.log('submit successfully', res)
                    localStorage.removeItem("userNameKey")
                    alert('logout all success')
                    navigate('/signIn')
                });
            }catch{
                console.log("err")
            }
        }

    }

    const navigate = useNavigate();
    React.useEffect(() =>  {
        if(localStorage.getItem('userNameKey') == null)
        {
            document.getElementById('signOutBtn').style.display = 'none';
        }
        else{
            document.getElementById('signInBtn').style.display = 'none';
            setUsername(localStorage.getItem("userNameKey"))
        }
    }, [])
    return (
        <Navbar expand="lg" className="bg-body-tertiary">
            <Container>
                <Navbar.Brand href="#home">Online Banking</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link onClick={() => navigate("/")}>Home</Nav.Link>
                            <Nav.Link onClick={() => navigate("/transactionPage")}>Transfer</Nav.Link>
                            <NavDropdown title="Manage" id="basic-nav-dropdown">
                                <NavDropdown.Item
                                    onClick={() => navigate("/manageDBPage")} >
                                    Manage Database
                                </NavDropdown.Item>
                                <NavDropdown.Item
                                    onClick={() => navigate("/tableManage")} >
                                    Manage Tablespace
                                </NavDropdown.Item>
                                <NavDropdown.Item
                                    onClick={() => navigate("/sessionManage")} >
                                    Manage Session
                                </NavDropdown.Item>
                                <NavDropdown.Item
                                    onClick={() => navigate("/manageUsersPage")} >
                                    Manage Users
                                </NavDropdown.Item>
                                <NavDropdown.Item
                                    onClick={() => navigate("/tablesManagement")} >
                                    Manage Tables
                                </NavDropdown.Item>
                                <NavDropdown.Item
                                    onClick={() => navigate("/auditManage")} >
                                    Manage audits
                                </NavDropdown.Item>
                                <NavDropdown.Item href="#">Another Action</NavDropdown.Item>
                            </NavDropdown>
                        </Nav>
                        <Nav>
                            <Nav.Link id="signInBtn" href="/signin">Signin</Nav.Link>
                            <Nav.Link id="usernameDisplay" href="/signin">{username}</Nav.Link>
                            <Nav.Link id="signOutBtn"><span onClick={onSignOutClick}>Signout</span></Nav.Link>
                            <Nav.Link id="signOutAllBtn"><span onClick={onSignOutAllClick}>Signout all</span></Nav.Link>
                        </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default NavbarComponent;