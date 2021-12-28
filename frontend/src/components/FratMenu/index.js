import React, { useEffect, useRef, useState } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';

// material-ui
import Autocomplete from '@mui/material/Autocomplete';
import { Typography } from '@mui/material';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import AdapterDateFns from '@mui/lab/AdapterDateFns';
import DateFnsUtils from '@date-io/date-fns';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import DesktopDatePicker from '@mui/lab/DesktopDatePicker';
import Grid from '@mui/material/Grid';
import LoadingButton from '@mui/lab/LoadingButton';
import CalculateIcon from '@mui/icons-material/Calculate';
// import { options as initialOptions } from "./options";

const initialOptions = [
  { label: '2016', value: '2016' },
  { label: '2017', value: '2017' },
  { label: '2018', value: '2018' },
  { label: '2019', value: '2019' },
  { label: '2020', value: '2020' },
  { label: '2021', value: '2021' }
];

export default function FratMenu() {

  const today = new Date();

  const [areFieldsFilled, setAreFieldsFilled] = useState(false);
  const [errorFlag, setErrorFlag] = useState(false)
  const [startDate, setStartDate] = React.useState(today);
  const [endDate, setEndDate] = React.useState(today);
  const [loading, setLoading] = React.useState(false);

  function handleClick() {
    setLoading(true);
  }

  const validateEndDate = (value) => {
    return value.getTime() > endDate.getTime() ? setStartDateValidation(true) : setStartDateValidation(false)
  }

  const validateStartDate = (value) => {
    return value.getTime() < startDate.getTime() ? setStartDateValidation(true) : setStartDateValidation(false)
  }

  const [isStartDateWrong, setStartDateValidation] = useState(false);

  const handleStartDateChange = (date) => {
    validateStartDate(date);
    setStartDate(date);
    console.log('zmiana startdate: ' + { date })
  };

  const handleEndDateChange = (date) => {
    validateEndDate(date);
    setEndDate(date);
    console.log('zmiana enddate ' + { date })
  };

  const checkIfFieldsAreFilled = () => {
    (startDate && endDate && !isStartDateWrong) ? setAreFieldsFilled(true) : setAreFieldsFilled(false)
  }

  useEffect(() => {
    checkIfFieldsAreFilled();
  })

  return (
    <Grid container direction="column" spacing={1} wrap="nowrap">
      <Grid container direction="row" spacing={1} wrap="nowrap" justifyContent="flex-start" alignItems="center">
        <Grid item xs={2}>
          <LocalizationProvider dateAdapter={AdapterDateFns}>
            <DesktopDatePicker
              label="Od"
              inputFormat="dd/MM/yyyy"
              value={startDate}
              onChange={handleStartDateChange}
              renderInput={(params) => <TextField {...params} />}
              maxDate={today}
            />
          </LocalizationProvider>
        </Grid>
        <Grid item xs={2}>
          <LocalizationProvider dateAdapter={AdapterDateFns}>
            <DesktopDatePicker
              label="Do"
              inputFormat="dd/MM/yyyy"
              value={endDate}
              onChange={handleEndDateChange}
              renderInput={(params) => <TextField {...params} />}
              maxDate={today}
            />
          </LocalizationProvider>
        </Grid>
        <Grid container item xs={2} justifyContent="flex-end" >
          <Autocomplete
            disablePortal
            id="year-select"
            options={initialOptions}
            sx={{ width: '25vh' }}
            renderInput={(params) => <TextField {...params} label="Oddział" />}
          />
        </Grid>
        <Grid container item xs={2} justifyContent="flex-end">
          <Autocomplete
            disablePortal
            id="year-select"
            options={initialOptions}
            sx={{ width: '25vh' }}
            renderInput={(params) => <TextField {...params} label="Badany materiał" />}
          />
        </Grid>
        <Grid container item xs={4} justifyContent="flex-end">
          <LoadingButton
            size="large"
            onClick={handleClick}
            endIcon={<CalculateIcon />}
            loading={loading}
            loadingPosition="end"
            variant="contained"
          >
            Analizuj
          </LoadingButton>
        </Grid>
      </Grid>
      <Grid container item wrap="nowrap" justifyContent="flex-start" spacing={1} style={{ paddingLeft: 0 }}>
        <Grid item>
          <Button style={{ textTransform: 'lowercase' }} variant="outlined" size="small">
            12 miesięcy
          </Button>
        </Grid>
        <Grid item>
          <Button style={{ textTransform: 'lowercase' }} variant="outlined" size="small">
            6 miesięcy
          </Button>
        </Grid>
        <Grid item>
          <Button style={{ textTransform: 'lowercase' }} variant="outlined" size="small">
            3 miesiące
          </Button>
        </Grid>
      </Grid>
    </Grid>
  );
}
