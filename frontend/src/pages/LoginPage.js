import * as React from 'react';

import { Paper, Button, Typography, Grid } from '@mui/material';
import { styled } from '@mui/material/styles';

import PersonIcon from '@mui/icons-material/Person';
import VpnKeyIcon from '@mui/icons-material/VpnKey';
import * as Yup from 'yup';
import { Formik, Form } from 'formik';

import TextfieldWrapper from '../components/Forms/Textfield';
import LoginButton from '../components/Forms/Button';
import LoginIcon from '@mui/icons-material/Login';

import BASE_URL from '../constants/BASE_URL';

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: 'flex-end',
}));


export default function LoginTab(props) {

  const [loading, setLoading] = React.useState(false);


  return (
    <>
      <DrawerHeader />
      <Formik
        initialValues={{ email: '', password: '' }}
        validationSchema={Yup.object({
          username: Yup.string('Niepoprawna nazwa użytkownika')
            .required('Wymagane'),
          password: Yup.string()
            .required('Wymagane')
        })}
        onSubmit={ async (values, { setSubmitting }) => {
          const res = await fetch(`${BASE_URL}/users`, {
              method: 'POST',
              mode: 'cors',
              headers: {
                  'Content-Type': 'application/json',
                  'Access-Control-Allow-Origin': '*'
              }}).then(response => response.json());
              body: JSON.stringify({

              })
          console.log(res);
          setTimeout(() => {
              alert(JSON.stringify(res, null, 2));
              setSubmitting(false);
          }, 400);
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
                <Typography variant="h6" sx={{ paddingBottom: "15px" }}>Logowanie</Typography>
              </Grid>
              <Grid container item spacing={2} alignItems="center">
                <Grid item>
                  <PersonIcon fontSize="medium" sx={{marginBottom: "1rem"}}/>
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="username" name="username" label="Nazwa użytkownika" type="text" fullWidth autoFocus />
                </Grid>
              </Grid>
              <Grid container item spacing={2} alignItems="center">
                <Grid item>
                  <VpnKeyIcon fontSize="medium" sx={{marginBottom: "1rem"}}/>
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="password" name="password" label="Hasło" type="password" fullWidth />
                </Grid>
              </Grid>
              <Grid container direction="column" alignItems="center" justifyContent="center">
                <Grid item>
                  <LoginButton icon={<LoginIcon />}>Zaloguj</LoginButton>
                </Grid>
                <Grid item>
                  <Button disableFocusRipple disableRipple variant="text" color="primary">Nie pamiętam hasła</Button>
                </Grid>
              </Grid>
            </Paper>
          </Grid>
        </Form>
      </Formik>
    </>
  );
}