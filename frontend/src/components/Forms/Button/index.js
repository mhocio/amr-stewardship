import React from "react";

// material-ui
import { LoadingButton } from "@mui/lab";

// third-party
import { useFormikContext } from "formik";

const ButtonWrapper = ({ children, loading, ...otherProps }) => {
  const { submitForm } = useFormikContext();

  const handleSubmit = () => {
    submitForm();
  };

  const configButton = {
    variant: "contained",
    onClick: handleSubmit,
    color: "primary",
    size: "large"
  };

  return (
    <LoadingButton
      {...configButton}
      loadingPosition="end"
      endIcon={otherProps.icon}
      loading={loading}
    >
      {children}
    </LoadingButton>
  );
};

export default ButtonWrapper;
