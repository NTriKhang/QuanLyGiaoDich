import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Home from './pages/HomePage/HomePage';
import Signin from './pages/SigninPage/SigninPage';
import './App.css';
import { useEffect, useState } from 'react';
import SigninPage from './pages/SigninPage/SigninPage';
import Signup from './pages/SignupPage/SignupPage';
import ManageDBPage from './pages/ManagePage/ManageDBPage';

function App() {
  const [loggedIn, setLoggedIn] = useState(false)
  const [email, setEmail] = useState("")

  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Home email={email} loggedIn={loggedIn} setLoggedIn={setLoggedIn}/>} />
          <Route path="/signin" element={<Signin setLoggedIn={setLoggedIn} setEmail={setEmail} />} />
          <Route path='/signup' element={<Signup/>}/>
          <Route path='/manageDBPage' element={<ManageDBPage/>} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;