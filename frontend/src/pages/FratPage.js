import React, { useState, useEffect } from "react";

// material-ui
import { styled } from "@mui/material/styles";
import Grid from "@mui/material/Grid";
import { Paper } from "@mui/material";

// project imports
import FratMenu from "../components/FratMenu";
import FratTable from "../components/Tables/FratTable";
import { DrawerHeader } from "../styledComponents/StyledDrawerHeader";

const FratPage = () => {
  const [fratData, setFratData] = useState([]);

  const handleSetFratData = (value) => {
    setFratData(value);
  };

  return (
    <>
      <DrawerHeader />
      <Grid container direction="column" justifyContent="" spacing={3}>
        <Grid item>
          <Paper sx={{ padding: "20px" }}>
            <FratMenu handleSetFratData={handleSetFratData} />
          </Paper>
        </Grid>
        <Grid item>
          {" "}
          <FratTable fratData={fratData} />
        </Grid>
      </Grid>
    </>
  );
};

export default FratPage;
