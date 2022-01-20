import PropTypes from 'prop-types';
import React, { useState } from "react";

// material-ui
import { Tabs, Tab, Box, Grid, Paper } from '@mui/material';

// project imports
import SusceptibilityChart from '../Charts/SusceptibilityChart';
import RecommendationChart from '../Charts/RecommendationChart';
import DosageChart from '../Charts/DosageChart';
import AntibioticGroupSelect from '../AntibioticGroupSelect';
import BacteriaSelect from '../BacteriaSelect';
import YearSelect from '../YearSelect';
import BASE_URL from '../../constants/BASE_URL';
import authHeader from "../../services/auth-header";
import { useLoading } from "../../loading/loading-context";

// third-party
import dayjs from 'dayjs';
import axios from 'axios';

function TabPanel(props) {
  const { children, value, index, ...other } = props;


  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          {children}
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.number.isRequired,
  value: PropTypes.number.isRequired,
};

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export default function BasicTabs() {
  const [value, setValue] = React.useState(0);
  const [chartData, setChartData] = useState([]);
  const { loading, setLoading } = useLoading();

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const handleSetChartData = (value) => {
    setChartData(value)
  }

  const handleSusceptibilityChart = (bacteria) => {
    setLoading(true);
    axios
      .get(`${BASE_URL}/chart/${bacteria}/${2022}/${dayjs().year()}`, {
        method: "GET",
        mode: "cors",
        headers: {
          Accept: "application/json, text/plain",
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          ...authHeader()
        }
      })
      .then((res) => {
        console.log(res.data);
        setChartData(modifySusceptibilityChartData(res.data));
      })
      .finally(() => {
        console.log(chartData);
        setLoading(false);
      });
  }

  const modifySusceptibilityChartData = (chartData) => {
    var resultList = [];
    var resultJson = {};
    for (var i = 0; i < chartData?.results?.length; i++) {
      resultJson = {};
      for (var j = 0; j < chartData?.keys?.length; j++) {
        resultJson[chartData?.keys[j]] = chartData?.results[i][j];
      }
      resultList.push(resultJson);
    }
    return resultList;
  }

  return (
    <Box sx={{ width: '100%', height: '500px' }}>
      <Box sx={{ marginLeft: '24px', marginRight: '24px' }}>
        <Paper sx={{ padding: '10px' }}>
          <Tabs value={value} onChange={handleChange} aria-label="basic tabs" centered>
          <Tab label="Lekowrażliwość drobnoustrojów" {...a11yProps(0)} />
            {/* <Tab label="Zmiany rekomendacji grup antybiotyków" {...a11yProps(1)} />
            <Tab label="DDD/1000 osobodni hospitalizacji" {...a11yProps(2)} /> */}
          </Tabs>
        </Paper>
      </Box>
      <TabPanel value={value} index={0}>
        <Grid container direction="row" spacing={2} wrap="nowrap">
          <Grid item xs={9}>
            <SusceptibilityChart chartData={chartData} />
          </Grid>
          <Grid item xs={3}>
            <Grid container direction="column" spacing={3} wrap="nowrap">
              <Grid item>
                <BacteriaSelect handleSusceptibilityChart={handleSusceptibilityChart}/>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      </TabPanel>
      {/* <TabPanel value={value} index={1}>
        <Grid container direction="row" spacing={2} wrap="nowrap">
          <Grid item xs={9}>
            <RecommendationChart />
          </Grid>
          <Grid item xs={3}>
            <Grid container direction="column" spacing={3} wrap="nowrap">
              <Grid item>
                <AntibioticGroupSelect />
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      </TabPanel>
      <TabPanel value={value} index={2}>
        <Grid container direction="row" spacing={2} wrap="nowrap">
          <Grid item xs={9}>
            <DosageChart />
          </Grid>
          <Grid item xs={3}>
            <Grid container direction="column" spacing={3} wrap="nowrap">
              <Grid item>
                <YearSelect />
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      </TabPanel> */}
    </Box>
  );
}
