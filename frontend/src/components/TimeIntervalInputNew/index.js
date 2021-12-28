import React from 'react';
import { Formik } from 'formik';
import * as Yup from 'yup';
import { Grid, TextField } from '@mui/material';

const validationSchema = Yup.object().shape({
  startDate: Yup.date()
    .required("Data jest wymagana"),

  endDate: Yup.date()
    .required("Data jest wymagana"),

  ward: Yup.string()
    .max(255, "Tekst nie może przekroczyć 255 znaków")
    .required("Pole nie może być puste"),

  material: Yup.string()
    .max(255, "Tekst nie może przekroczyć 255 znaków")
    .required("Pole nie może być puste"),

})

export default function TimeIntervalInputNew() {

  return (
    <Formik
      initialValues={{ startDate: "", endDate: "", ward: "", material: "" }}
      validationSchema={validationSchema}
      onSubmit={(values, { setSubmitting, resetForm }) => {
        setTimeout(() => {
          alert(JSON.stringify(values, null, 2));
          setSubmitting(false);
        }, 400);
      }}
    >
      {({
        values,
        errors,
        touched,
        handleChange,
        handleBlur,
        handleSubmit,
        isSubmitting
      }) => (
        <form onSubmit={handleSubmit}>
          <Grid container direction="row" spacing={6} wrap="nowrap">
            <Grid item xs={3}>
              <TextField
                id="eventName"
                label="Event Name"
                margin="normal"
                variant="filled"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.eventName}
                className={touched.eventName && errors.eventName ? "has-error" : null}
              />
              {touched.eventName}{errors.eventName}
            </Grid>

            <Grid item xs={3}>
              <TextField
                id="startDate"
                label="Event Start Date"
                type="datetime-local"
                InputLabelProps={{
                  shrink: true
                }}
                format="dd-MM-YYYY"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.startDate}
              />
              {touched.startDate} {errors.startDate}
            </Grid>

            <Grid item xs={3}>
              <TextField
                id="endDate"
                label="Event End Date"
                type="datetime-local"
                InputLabelProps={{
                  shrink: true
                }}
                format="dd-MM-YYYY"
                onChange={handleChange}
                onBlur={handleBlur}
                value={values.endDate}
              />
              {touched.endDate} {errors.endDate}
            </Grid>


            <Grid item xs={3}>
              <button type="submit" disabled={isSubmitting} >
                Submit
              </button>
            </Grid>
          </Grid>

        </form>
      )}
    </Formik>
  )

}