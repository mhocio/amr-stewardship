import React, { useState, useEffect } from "react";

// material-ui
import { styled } from "@mui/material/styles";
import Grid from "@mui/material/Grid";
import { Paper, Typography, Box, Skeleton } from "@mui/material";

// project imports
import FratMenu from "../components/FratMenu";
import FratTable from "../components/Tables/FratTable";
import { DrawerHeader } from "../styledComponents/StyledDrawerHeader";
import FratTablePlaceholder from "../components/FratTablePlaceholder";
import { useLoading } from "../loading/loading-context";

const FratPage = () => {
  const [fratData, setFratData] = useState([]);
  const { loading, setLoading } = useLoading();

  const handleSetFratData = (value) => {
    setFratData(value);
  };


  var skeletons = [];
  for (var i = 0; i < 8; i++) {
    skeletons.push(<Skeleton sx={{ height: 40 }} key={i} />);
  }

  return (
    <>
      <DrawerHeader />
      <Grid container direction="column" spacing={3}>
        <Grid item>
          <Paper sx={{ padding: "20px" }}>
            <FratMenu
              handleSetFratData={handleSetFratData}
            />
          </Paper>
        </Grid>
        <Grid item>
          {fratData.antibiotics ? (
            <FratTable fratData={fratData} />
          ) : loading ? (
            <Box sx={{ height: "100%" }}>
              <Skeleton height={100} />
              {skeletons}
            </Box>
          ) : (
            <FratTablePlaceholder />
          )}
        </Grid>
      </Grid>
    </>
  );
};

export default FratPage;
