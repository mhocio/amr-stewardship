import * as React from 'react';
import Typography from '@mui/material/Typography';

import { Paper, TextField, Button } from '@mui/material';
import { styled } from '@mui/material/styles';
import Grid from '@mui/material/Grid';
import AlternateEmailIcon from '@mui/icons-material/AlternateEmail';
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import * as Yup from 'yup';
import { Formik, Form } from 'formik';

import TextfieldWrapper from '../components/Forms/Textfield';

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: 'flex-end',
}));


export default function LoginTab(props) {
  return (
    <>
      <DrawerHeader />
      <Formik
        initialValues={{ email: '', password: '' }}
        validationSchema={Yup.object({
          email: Yup.string()
            .email('Niepoprawny adres email')
            .required('Wymagane'),
          password: Yup.string()
            .required('Wymagane')
        })}
        onSubmit={values => {
          console.log(values);
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
                <Typography variant="h6" sx={{ paddingBottom: "10px" }}>Logowanie</Typography>
              </Grid>
              <Grid container spacing={2} alignItems="center">
                <Grid item>
                  <AlternateEmailIcon fontSize="medium" />
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="email" name="email" label="Email" type="email" fullWidth autoFocus />
                </Grid>
              </Grid>
              <Grid container spacing={2} alignItems="center">
                <Grid item>
                  <VpnKeyIcon fontSize="medium" />
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="password" name="password" label="Hasło" type="password" fullWidth />
                </Grid>
              </Grid>
              <Grid container direction="column" alignItems="center" justifyContent="center" style={{ marginTop: '25px' }}>
                <Grid item>
                  <Button variant="contained" color="primary" style={{ textTransform: "none", width: "30vh" }}>Zaloguj</Button>
                </Grid>
                <Grid item>
                  <Button disableFocusRipple disableRipple style={{ textTransform: "none" }} variant="text" color="primary">Nie pamiętam hasła</Button>
                </Grid>
              </Grid>
            </Paper>
          </Grid>
        </Form>
      </Formik>
    </>
  );
}