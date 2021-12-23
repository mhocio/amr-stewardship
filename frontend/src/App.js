import './styles/App.css';
import React , { Component}  from 'react';
import {
  Routes,
  Route,
  BrowserRouter,
  Navigate,
  Outlet
} from "react-router-dom";
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { plPL } from '@mui/x-data-grid';
import { blue, yellow } from '@mui/material/colors';
import MainLayout from './components/MainLayout'

import PatientsPage from './pages/PatientsPage';
import TrendsPage from './pages/TrendsPage';

const theme = createTheme({
  palette: {
    primary: {
      main: blue[900],
    },
    secondary: {
      main: yellow[600],
    },
  },
},
  plPL
);

function PrivateOutlet() {
  const auth = useAuth();
  return auth ? <Outlet /> : <Navigate to="/login" />;
}

function PrivateRoute({ children }) {
  const auth = useAuth();
  return auth ? children : <Navigate to="/login" />;
}

const Public = () => <div>public</div>;
const Private = () => <div>private</div>;
const Login = () => <div>login</div>;

function useAuth() {
  return true;
}

function App() {
  return (
    <ThemeProvider theme={theme}>
      <BrowserRouter>
      <MainLayout>
        <Routes>
          <Route path="/patients" element={<PatientsPage />} />
          <Route path="/trends" element={<TrendsPage />} />
          <Route
            path="/private"
            element={
              <PrivateRoute>
                <Private />
              </PrivateRoute>
            }
          />
        </Routes>
        </MainLayout>
        </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
