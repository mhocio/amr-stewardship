import * as React from "react";

// material-ui
import { Autocomplete, Paper, TextField } from "@mui/material";

// project imports
import BASE_URL from "../../constants/BASE_URL";
import authHeader from "../../services/auth-header";

// third-party
import dayjs from "dayjs";
import axios from "axios";

const getYears = (startYear) => {
  var currentYear = dayjs().year();
  var years = [];

  while (startYear <= currentYear) {
    years.push(startYear++);
  }

  return years;
};


var years = getYears(2000);
var yearOptions = [];
for (var i = 0; i < years?.length; i++) {
  var row = {};
  row["label"] = years[i].toString();
  row["value"] = years[i].toString();
  yearOptions.push(row);
}


export default function YearSelect({handleSetYear, label}) {
  return (
    <Paper sx={{ padding: "20px" }}>
      <Autocomplete
        disablePortal
        id="year-select"
        options={yearOptions}
        onChange={(event, newValue) => {
          handleSetYear(newValue?.value);
        }}
        renderInput={(params) => <TextField {...params} label={label} />}
      />
    </Paper>
  );
}
