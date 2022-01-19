import React, { useState, useEffect, useRef } from "react";
// material-ui
import { Grid, Paper, Button } from "@mui/material";
import { LoadingButton } from "@mui/lab";

// project imports
import PatientsTable from "../components/Tables/PatientsTable";
import ExamTable from "../components/Tables/ExamTable";
import BASE_URL from "../constants/BASE_URL";
import { DrawerHeader } from "../styledComponents/StyledDrawerHeader";
import axios from "../services/interceptor";

import { useLoading } from "../loading/loading-context";

import authHeader from "../services/auth-header";

const PatientsPage = () => {
  const [patients, setPatients] = useState([]);
  const [antibiograms, setAntibiograms] = useState([]);
  const { loading, setLoading } = useLoading();
  const uploadInputRef = useRef(null);


  const handlePatientAntibiograms = (params) => {
    setAntibiograms(params);
  };

  const handleClick = event => {
    uploadInputRef.current.click();
    console.log("handled click");
  };
  const handleChange = event => {
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
    formData.append('file', blob);
    axios
      .post(`${BASE_URL}/antibiogram/import`, formData, {
        method: "POST",
        mode: "cors",
        headers: {
          "Content-Type": "multipart/form-data;",
          ...authHeader()
        }
      })
      .then((res) => {
        console.log(res)
        
      })
      .catch((err) => {
        console.log(err)
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
          <input
            ref={uploadInputRef}
            type="file"
            accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            style={{ display: "none" }}
            onChange={handleChange}
          />
          <Button
            onClick={handleClick}
            variant="contained"
          >
            Importuj antybiogramy
          </Button>
        </Paper>
      </Grid>
    </>
  );
};

export default PatientsPage;
