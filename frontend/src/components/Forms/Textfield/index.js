import React from "react";

// material-ui
import { TextField } from "@mui/material";

// third-party
import { useField } from "formik";

const TextfieldWrapper = ({ name, ...otherProps }) => {
  const [field, mata] = useField(name);

  const configTextField = {
    ...field,
    ...otherProps,
    fullWidth: true,
    variant: "outlined"
  };

  if (mata && mata.touched && mata.error) {
    configTextField.error = true;
    configTextField.helperText = mata.error;
  }

  return <TextField {...configTextField} style={{ minHeight: "5rem" }} />;
};

export default TextfieldWrapper;
