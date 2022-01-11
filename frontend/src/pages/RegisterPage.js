import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

// material-ui
import { Paper, Typography, Grid } from '@mui/material';
import { styled } from '@mui/material/styles';
import AlternateEmailIcon from '@mui/icons-material/AlternateEmail';
import PersonIcon from '@mui/icons-material/Person';
import AppRegistrationIcon from '@mui/icons-material/AppRegistration';
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';

// project imports
import RegisterButton from '../components/Forms/Button';
import TextfieldWrapper from '../components/Forms/Textfield';
import BASE_URL from '../constants/BASE_URL';

// third-party
import * as Yup from 'yup';
import { Formik, Form } from 'formik';
import axios from 'axios';

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: 'flex-end',
}));


export default function RegisterTab(props) {

  const [errorFlag, setErrorFlag] = useState(false);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  return (
    <>
      <DrawerHeader />
      <Formik
        initialValues={{ username: '', email: '', password: '', passwordConfirm: '' }}
        validationSchema={Yup.object({
          email: Yup.string()
            .email('Niepoprawny adres email')
            .required('Wymagane'),
          username: Yup.string('Niepoprawna nazwa użytkownika')
            .required('Wymagane'),
          password: Yup.string()
            .required('Wymagane'),
          passwordConfirm: Yup.string()
            .oneOf([Yup.ref('password'), null], "Hasła są różne")
            .required('Wymagane'),
        })}
        onSubmit={values => {
          axios.post(`${BASE_URL}/auth/signup`, {
            "username": values.username,
            "email": values.email,
            "password": values.password
          }
          )
            .then(res => {
              if (res.data.authenticationToken) {
                localStorage.setItem("user", JSON.stringify(res.data));
              }
              navigate('/login');
            })
        }}
      >
        <Form>
          <Grid container spacing={4} alignItems="center" justifyContent="center">
            <Paper style={{
              position: 'absolute', left: '50%', top: '50%',
              transform: 'translate(-50%, -50%)',
              padding: '30px'
            }}>
              <Grid item>
                <Typography variant="h6" sx={{ paddingBottom: "15px" }}>Rejestracja</Typography>
              </Grid>
              <Grid container item spacing={2} alignItems="center">
                <Grid item>
                  <AlternateEmailIcon fontSize="medium" sx={{ marginBottom: "1rem" }} />
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="email" name="email" label="Email" type="email" fullWidth autoFocus />
                </Grid>
              </Grid>
              <Grid container item spacing={2} alignItems="center">
                <Grid item>
                  <PersonIcon fontSize="medium" sx={{ marginBottom: "1rem" }} />
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="username" name="username" label="Nazwa użytkownika" type="text" fullWidth />
                </Grid>
              </Grid>
              <Grid container item spacing={2} alignItems="center">
                <Grid item>
                  <VpnKeyIcon fontSize="medium" sx={{ marginBottom: "1rem" }} />
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="password" name="password" label="Hasło" type="password" fullWidth />
                </Grid>
              </Grid>
              <Grid container item spacing={2} alignItems="center">
                <Grid item>
                  <VpnKeyIcon fontSize="medium" sx={{ marginBottom: "1rem" }} />
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="passwordConfirm" name="passwordConfirm" label="Powtórz hasło" type="password" fullWidth />
                </Grid>
              </Grid>
              <Grid container direction="column" alignItems="center" justifyContent="center">
                <Grid item>
                  <RegisterButton icon={<AppRegistrationIcon />}>Zarejestruj</RegisterButton>
                </Grid>
              </Grid>
            </Paper>
          </Grid>
        </Form>
      </Formik>
    </>
  );
}