import React, { useEffect, useRef, useState } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';

// material-ui
import Autocomplete from '@mui/material/Autocomplete';
import { Typography } from '@mui/material';
import TextField from '@mui/material/TextField';
import AdapterDateFns from '@mui/lab/AdapterDateFns';
import DateFnsUtils from '@date-io/date-fns';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import DesktopDatePicker from '@mui/lab/DesktopDatePicker';
import Grid from '@mui/material/Grid';
import { Button } from '@mui/material';

// project imports
import MyDatePicker from '../Forms/DatePicker'
import AnalyzeButton from '../Forms/Button';
import MySelect from '../Forms/Select'

// dummy data
import materials from '../data/materials.json'
import wards from '../data/wards.json'

const initialValues = {
  startDate: '',
  endDate: '',
  material: '',
  ward: ''
}

const validationSchema = Yup.object({
  startDate: Yup.date()
    .required("Data początkowa jest wymagana"),
  endDate: Yup.date()
    .when(
      "startDate",
      (startDate, schema) => startDate && schema.min(startDate))
    .required("Must enter end date"),
  material: Yup.string()
    .required('Badany materiał jest wymagany'),
  ward: Yup.string()
    .required('Oddział jest wymagany')
})

export default function FratMenu() {

  const today = new Date();

  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema}
      validateOnBlur={false}
      validateOnChange
      onSubmit={values => {
        console.log(values)
      }}>
      <Form>
        <Grid container direction="column" spacing={1} wrap="nowrap">
          <Grid container direction="row" spacing={1} wrap="nowrap" justifyContent="flex-start" alignItems="center">
            <Grid item xs={2}>
              <MyDatePicker
                name="startDate"
                label="Od"
              />
            </Grid>
            <Grid item xs={2}>
              <MyDatePicker
                name="endDate"
                label="Do"
              />
            </Grid>
            <Grid container item xs={2} justifyContent="flex-end" >
              <MySelect
                name="ward"
                label="Oddział"
                options={wards}
              />
            </Grid>
            <Grid container item xs={2} justifyContent="flex-end">
              <MySelect
                name="material"
                label="Badany materiał"
                options={materials}
              />
            </Grid>
            <Grid container item xs={4} justifyContent="flex-end">
              <AnalyzeButton>
                Analizuj
              </AnalyzeButton>
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
      </Form>
    </Formik>
  );
}
