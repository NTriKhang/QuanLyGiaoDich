import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Home from './pages/HomePage/HomePage';
import Signin from './pages/SigninPage/SigninPage';
import './App.css';
import { useState } from 'react';
import Signup from './pages/SignupPage/SignupPage';
import ManageDBPage from './pages/ManagePage/ManageDBPage';
import ManageUsersPage from './pages/ManagePage/ManageUsersPage';
import UserDetailAdminPage from './pages/ManagePage/UserDetailAdminPage';
import TablespaceManagement from './pages/TableSpacePage/TablespaceManagement';
import SessionPage from './pages/SessionPage/SessionPage';
import TablesManagement from './pages/TablesPage/TablesManagementPage';
import ManageAuditPage from './pages/AuditPage/ManageAuditPage';
import AddAuditPage from './pages/AuditPage/AddAuditPage';

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
          <Route path='/manageUsersPage' element={<ManageUsersPage/>} />
          <Route path='/userDetailAdminPage' element={<UserDetailAdminPage/>} />
          <Route path='/tableManage' element={<TablespaceManagement/>} />
          <Route path='/tablesManagement' element={<TablesManagement/>} />
          <Route path='/sessionManage' element={<SessionPage/>} />
          <Route path='/auditManage' element={<ManageAuditPage/>} />
          <Route path='/addAudit' element={<AddAuditPage/>} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;