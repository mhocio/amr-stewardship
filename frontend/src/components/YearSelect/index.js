import * as React from "react";

// material-ui
import { Autocomplete, Paper, TextField } from "@mui/material";

// project imports
import BASE_URL from "../../constants/BASE_URL";
import authHeader from "../../services/auth-header";

// third-party
import dayjs from "dayjs";
import axios from "axios";

const initialOptions = [
  { label: "2016", value: "2016" },
  { label: "2017", value: "2017" },
  { label: "2018", value: "2018" },
  { label: "2019", value: "2019" },
  { label: "2020", value: "2020" },
  { label: "2021", value: "2021" }
];

const getYears = (startYear) => {
  var currentYear = dayjs().year();
  var years = [];

  startYear = startYear || 2020;
  while (startYear <= currentYear) {
    years.push(startYear++);
  }

  return JSON.stringify(years);
};

export default function YearSelect() {
  const onOptionClickHandle = () => {
    axios.get(`${BASE_URL}/patient`, {
      method: "GET",
      mode: "cors",
      headers: {
        Accept: "application/json, text/plain",
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        ...authHeader()
      }
    });
  };

  return (
    <Paper sx={{ padding: "20px" }}>
      <Autocomplete
        disablePortal
        id="year-select"
        options={initialOptions}
        renderInput={(params) => <TextField {...params} label="Rok" />}
      />
    </Paper>
  );
}
