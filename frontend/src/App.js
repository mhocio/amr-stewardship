import './styles/App.css';
import { Route, Routes } from "react-router-dom";

import { ThemeProvider } from '@material-ui/core';
import { createTheme } from '@material-ui/core/styles';
import { plPL } from '@mui/material/locale';

import MainPage from './pages/MainPage';

const Theme = createTheme({
  palette: {
    primary: {
      main: '#24345c',
    },
    secondary: {
      main: '#fd472c',
    },
    contrastThreshold: 3,
    tonalOffset: 0.2,
  },
},
plPL);

function App() {
  return (
    <ThemeProvider theme={Theme}>
      <Routes>
        <Route path="/main" element={<MainPage/>} />
      </Routes>
    </ThemeProvider>
  );
}

export default App;
