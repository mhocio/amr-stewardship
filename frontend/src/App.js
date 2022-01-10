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
import { bool } from 'yup';

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

function useAuth() {
  const user = JSON.parse(localStorage.getItem("user"));
  var ret = (user && user.authenticationToken) ? true : false;
  console.log('useAuth: ' + ret);
  return ret;
}

function PrivateOutlet() {
  const auth = useAuth();
  return auth ? <Outlet /> : <Navigate to="/login" />;
}

function App() {

  return (
    <ThemeProvider theme={theme}>
      <BrowserRouter>
        <MainLayout isAuth={useAuth()}>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/page" element={<PrivateOutlet />}>
              <Route path="/page/frat" element={<FratPage />} />
              <Route path="/page/trends" element={<TrendsPage />} />
              <Route path="/page/patients" element={<PatientsPage />} />
            </Route>
          </Routes>
        </MainLayout>
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
