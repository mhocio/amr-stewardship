import React from 'react';

// material-ui
import { Checkbox, FormControl, FormControlLabel, FormGroup, FormLabel } from '@mui/material';

// third-party
import { useField, useFormikContext } from 'formik';

const CheckboxWrapper = ({ name, label, legend, ...otherProps }) => {

  const [field, meta] = useField(name);
  const { setFieldValue } = useFormikContext();
  const handleChange = event => {
    const { checked } = event.target;
    setFieldValue(name, checked);
  }

  const configCheckbox = {
    ...field,
    onChange: handleChange
  }

  const configFormControl = {};

  if (meta && meta.error && meta.touched) {
    configFormControl.error = true;
  }

  return (
    <FormControl {...configFormControl}>
      <FormLabel component="legend">{legend}</FormLabel>
      <FormGroup>
        <FormControlLabel
          control={<Checkbox {...configCheckbox} />}
          label={label}
        />
      </FormGroup>
    </FormControl>
  )
}

export default CheckboxWrapper;