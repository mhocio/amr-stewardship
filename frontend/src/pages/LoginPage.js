import * as React from "react";
import { useNavigate } from "react-router-dom";

// material-ui
import { Paper, Button, Typography, Grid } from "@mui/material";
import { styled } from "@mui/material/styles";
import PersonIcon from "@mui/icons-material/Person";
import VpnKeyIcon from "@mui/icons-material/VpnKey";
import LoginIcon from "@mui/icons-material/Login";

// project imports
import TextfieldWrapper from "../components/Forms/Textfield";
import LoginButton from "../components/Forms/Button";
import BASE_URL from "../constants/BASE_URL";
import { DrawerHeader } from "../styledComponents/StyledDrawerHeader";
import { useLoading } from "../loading/loading-context";
import { useSnackbar } from "notistack";

// third-party
import * as Yup from "yup";
import { Formik, Form } from "formik";
import axios from "axios";

export default function LoginTab() {
  const { loading, setLoading } = useLoading();
  const nagivate = useNavigate();
  const { enqueueSnackbar, closeSnackbar } = useSnackbar();

  return (
    <>
      <DrawerHeader />
      <Formik
        initialValues={{ email: "", password: "" }}
        validationSchema={Yup.object({
          username: Yup.string("Niepoprawna nazwa użytkownika").required(
            "Wymagane"
          ),
          password: Yup.string().required("Wymagane")
        })}
        onSubmit={(values) => {
          setLoading(true);
          axios
            .post(`${BASE_URL}/auth/login`, {
              username: values.username,
              password: values.password
            })
            .then((res) => {
              if (res.data.authenticationToken) {
                localStorage.setItem("user", JSON.stringify(res.data));
              }
              setLoading(false);
              window.location.reload();
            })
            .catch((error) => {
              setLoading(false);
              enqueueSnackbar(error.response.data.message, {
                variant: "error"
              });
            })
        }}
      >
        <Form>
          <Grid
            container
            spacing={4}
            alignItems="center"
            justifyContent="center"
          >
            <Paper
              style={{
                position: "absolute",
                left: "50%",
                top: "50%",
                transform: "translate(-50%, -50%)",
                padding: "30px"
              }}
            >
              <Grid item>
                <Typography variant="h6" sx={{ paddingBottom: "15px" }}>
                  Logowanie
                </Typography>
              </Grid>
              <Grid container item spacing={2} alignItems="center">
                <Grid item>
                  <PersonIcon fontSize="medium" sx={{ marginBottom: "1rem" }} />
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="username"
                    name="username"
                    label="Nazwa użytkownika"
                    type="text"
                    fullWidth
                    autoFocus
                  />
                </Grid>
              </Grid>
              <Grid container item spacing={2} alignItems="center">
                <Grid item>
                  <VpnKeyIcon fontSize="medium" sx={{ marginBottom: "1rem" }} />
                </Grid>
                <Grid item>
                  <TextfieldWrapper
                    id="password"
                    name="password"
                    label="Hasło"
                    type="password"
                    fullWidth
                  />
                </Grid>
              </Grid>
              <Grid
                container
                direction="column"
                alignItems="center"
                justifyContent="center"
              >
                <Grid item>
                  <LoginButton type="submit" icon={<LoginIcon />}>
                    Zaloguj
                  </LoginButton>
                </Grid>
                <Grid item sx={{ marginTop: "15px" }}>
                  <Button
                    disableFocusRipple
                    disableRipple
                    variant="text"
                    color="primary"
                    onClick={() => nagivate("/auth/register")}
                  >
                    Nie mam konta
                  </Button>
                </Grid>
                <Grid item>
                  <Button
                    disableFocusRipple
                    disableRipple
                    variant="text"
                    color="primary"
                  >
                    Nie pamiętam hasła
                  </Button>
                </Grid>
              </Grid>
            </Paper>
          </Grid>
        </Form>
      </Formik>
    </>
  );
}
