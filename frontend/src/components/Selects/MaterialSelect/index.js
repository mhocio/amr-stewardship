import React, { useState, useEffect } from "react";

// material-ui
import { Paper, TextField, Autocomplete } from "@mui/material";

// project imports
import BASE_URL from "../../../constants/BASE_URL";
import authHeader from "../../../services/auth-header";

// third-party
import axios from "axios";

export default function MaterialSelect({ handleSetMaterial }) {
  const [material, setMaterial] = useState([]);
  const [errorFlag, setErrorFlag] = useState(false);

  useEffect(() => {
    getAllMaterials();
  }, []);

  const getAllMaterials = () => {
    axios
      .get(`${BASE_URL}/material`, {
        method: "GET",
        mode: "cors",
        headers: {
          Accept: "application/json, text/plain",
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          ...authHeader()
        }
      })
      .then((res) => {
        console.log(res.data);
        setMaterial(res.data);
      })
      .catch((err) => {
        setErrorFlag(true);
      });
  };

  return (
    <Paper sx={{ padding: "20px" }}>
      <Autocomplete
        disablePortal
        id="material-select"
        getOptionLabel={(option) => option.name}
        getOptionValue={(option) => option.materialId}
        options={material}
        onChange={(event, newValue) => {
          handleSetMaterial(newValue?.name);
        }}
        //defaultValue={newValue ? newValue : null}
        renderInput={(params) => <TextField {...params} label="Materiał" />}
      />
    </Paper>
  );
}
