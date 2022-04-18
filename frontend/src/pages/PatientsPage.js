import React, { useState, useEffect, useRef } from "react";

// material-ui
import {
  Grid,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions
} from "@mui/material";

// project imports
import PatientsTable from "../components/Tables/PatientsTable";
import ExamTable from "../components/Tables/ExamTable";
import BASE_URL from "../constants/BASE_URL";
import { DrawerHeader } from "../styledComponents/StyledDrawerHeader";
import { useLoading } from "../loading/loading-context";
import authHeader from "../services/auth-header";
import axios from "../services/interceptor";
import MySelect from "../components/Forms/Select";

// third-party
import { Formik, Form } from "formik";
import * as Yup from "yup";


const PatientsPage = () => {
  const [patients, setPatients] = useState([]);
  const [antibiograms, setAntibiograms] = useState([]);
  const { loading, setLoading } = useLoading();
  const uploadInputRef = useRef(null);
  const [open, setOpen] = React.useState(false);

  const [antibiogramType, setAntibiogramType] = React.useState("");

  const antibiogramTypeOptions = [
    { antibiogramTypeId: 1, name: "CGM" },
    { antibiogramTypeId: 2, name: "ASSECO" }
  ];

  const initialValues = {
    antibiogramType: ""
  };

  const validationSchema = Yup.object({
    antibiogramType: Yup.string().required("Rodzaj danych jest wymagany"),
  });

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = (event, reason) => {
    if (reason !== "backdropClick") {
      setOpen(false);
    }
  };

  const handlePatientAntibiograms = (params) => {
    setAntibiograms(params);
  };

  const handleClick = (event) => {
    uploadInputRef.current.click();
    console.log("handled click");
    handleClose();
  };

  const handleChange = (event) => {
    handleImportData(event.target.files[0]);
    console.log("handled change");
  };

  useEffect(() => {
    getTablesData();
  }, []);

  const handleImportData = (blob) => {
    console.log("start file upload");
    console.log(blob);
    setLoading(true);
    const formData = new FormData();
    formData.append("file", blob);
    axios
      .post(`${BASE_URL}/antibiogram/import/${antibiogramType}`, formData, {
        method: "POST",
        mode: "cors",
        headers: {
          "Content-Type": "multipart/form-data;",
          ...authHeader()
        }
      })
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
        getTablesData();
        setLoading(false);
      });
  };

  const getTablesData = async () => {
    setLoading(true);
    axios
      .all([
        await axios.get(`${BASE_URL}/patient`, {
          method: "GET",
          mode: "cors",
          headers: {
            Accept: "application/json, text/plain",
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            ...authHeader()
          }
        }),
        await axios.get(`${BASE_URL}/antibiogram`, {
          method: "GET",
          mode: "cors",
          headers: {
            Accept: "application/json, text/plain",
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            ...authHeader()
          }
        })
      ])
      .then(
        axios.spread((res1, res2) => {
          res1.data.map((e) => {
            e["id"] = e.pesel;
          });
          res2.data.map((e) => {
            e["id"] = e.antibiogramId;
          });
          setPatients(res1.data);
          setAntibiograms(res2.data);
        })
      )
      .catch((err) => {
        console.log(`Couldn't fetch tables data: ${err}`);
      })
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <>
      <DrawerHeader />
      <Grid container spacing={2}>
        <Grid item xs={3.5}>
          <PatientsTable
            data={patients}
            loading={loading}
            handlePatientAntibiograms={handlePatientAntibiograms}
          />
        </Grid>
        <Grid item xs={8.5}>
          <ExamTable data={antibiograms} loading={loading} />
        </Grid>
      </Grid>
      <Grid item>
        <Paper
          sx={{
            position: "absolute",
            bottom: 0,
            right: 0,
            padding: "20px",
            margin: "25px"
          }}
        >
          <Formik
            initialValues={initialValues}
            validationSchema={validationSchema}
            validateOnBlur={false}
            validateOnChange
          >
            {({ isSubmitting, dirty, setFieldValue }) => (
              <Form>
                <Button onClick={handleClickOpen} variant="contained">
                  Importuj antybiogramy
                </Button>
                <Dialog
                  maxWidth="xs"
                  fullWidth
                  disableEscapeKeyDown
                  open={open}
                  onClose={handleClose}
                >
                  <DialogTitle>Import danych</DialogTitle>
                  <DialogContent
                    sx={{ alignItems: "center", justifyContent: "center" }}
                  >
                    <Grid
                      container
                      direction="row"
                      spacing={1}
                      wrap="nowrap"
                      justifyContent="center"
                      alignItems="center"
                      sx={{ paddingTop: "10px" }}
                    >
                      <Grid item>
                        <MySelect
                          name="antibiogramType"
                          label="Typ danych"
                          sx={{ width: "200px" }}
                          onClick={(e)=>setAntibiogramType(e.target.textContent.toLowerCase())} // <Formik/> doesn't support onChange XD - 16.04.2022
                          options={antibiogramTypeOptions.map((item, index) => (
                            <option key={index} value={item.name}>
                              {item.name}
                            </option>
                          ))}
                        />
                      </Grid>
                    </Grid>
                  </DialogContent>
                  <DialogActions>
                    <Button onClick={handleClose}>Zamknij</Button>
                    <Button onClick={handleClick} variant="contained">
                      Importuj
                    </Button>
                  </DialogActions>
                </Dialog>
                <input
                  ref={uploadInputRef}
                  type="file"
                  accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                  style={{ display: "none" }}
                  onChange={handleChange}
                />
              </Form>
            )}
          </Formik>
        </Paper>
      </Grid>
    </>
  );
};

export default PatientsPage;
