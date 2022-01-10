import './styles/App.css';
import React, { useEffect, useState } from 'react';
import {
  Routes,
  Route,
  BrowserRouter,
  Navigate,
  Outlet
} from "react-router-dom";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { plPL } from '@mui/x-data-grid';
import { blue } from '@mui/material/colors';
import MainLayout from './components/MainLayout'

import PatientsPage from './pages/PatientsPage';
import TrendsPage from './pages/TrendsPage';
import FratPage from './pages/FratPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import { AuthContext } from './context/auth';

const theme = createTheme({
  palette: {
    primary: {
      main: blue[900],
    },
    secondary: {
      main: '#B6CEFF',
    },
    background: {
      default: "#F6F9FF"
    },
  },
  typography: {
    button: {
      textTransform: 'none'
    }
  },
  shape: {
    borderRadius: 20,
  },
},
  plPL
);

function isLoggedIn() {
  const user = JSON.parse(localStorage.getItem("user"));
  var ret = (user && user.authenticationToken) ? true : false;
  console.log('useAuth: ' + ret);
  return ret;
}

function PrivateOutlet() {
  const auth = isLoggedIn();
  return auth ? <Outlet /> : <Navigate to="/auth" />;
}

function LoginOutlet() {
  const auth = isLoggedIn();
  return !auth ? <Outlet /> : <Navigate to="/page" />;
}

function App() {


  const existingUser = JSON.parse(localStorage.getItem("user"));
  const [user, setUser] = useState(existingUser);

  const setToken = (data) => {
    localStorage.setItem("authenticationToken", data)
    setUser(data)
  }

  return (
    <AuthContext.Provider value={{ user, setUser: setToken }}>
      <ThemeProvider theme={theme}>
        <BrowserRouter>
          <MainLayout>
            <Routes>

              <Route
                path="/auth"
                element={<Navigate to="/auth/login" />}
              />

              <Route
                path="/page"
                element={<Navigate to="/page/patients" />}
              />

              <Route path="/auth" element={<LoginOutlet />}>
                <Route path="/auth/login" element={<LoginPage />} />
                <Route path="/auth/register" element={<RegisterPage />} />
              </Route>

              <Route path="/page" element={<PrivateOutlet />}>
                <Route path="/page/frat" element={<FratPage />} />
                <Route path="/page/trends" element={<TrendsPage />} />
                <Route path="/page/patients" element={<PatientsPage />} />
              </Route>

              <Route
                path="*"
                element={<Navigate to="/auth" />}
              />

            </Routes>
          </MainLayout>
        </BrowserRouter>
      </ThemeProvider>
    </AuthContext.Provider>
  );
}

export default App;
