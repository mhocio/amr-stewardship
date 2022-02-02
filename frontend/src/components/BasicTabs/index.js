import PropTypes from 'prop-types';
import React, { useEffect, useState } from "react";

// material-ui
import { Tabs, Tab, Box, Grid, Paper } from '@mui/material';

// project imports
import SusceptibilityChart from '../Charts/SusceptibilityChart';
import RecommendationChart from '../Charts/RecommendationChart';
import DosageChart from '../Charts/DosageChart';
import AntibioticGroupSelect from '../AntibioticGroupSelect';
import BacteriaSelect from '../BacteriaSelect';
import MaterialSelect from '../MaterialSelect';
import WardSelect from '../WardSelect';
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
  const [materialChartData, setMaterialChartData] = useState([]);
  const [graphChartData, setGraphChartData] = useState([]);
  const { loading, setLoading } = useLoading();

  const [material, setMaterial] = useState('');
  const [ward, setWard] = useState('');
  const [startYear, setStartYear] = useState('');
  const [endYear, setEndYear] = useState('');

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const handleSetChartData = (value) => {
    setChartData(value)
  }

  function updateFRATcharts() {
    if (material && ward && startYear && endYear)
      handleMaterialChart(material, ward, startYear, endYear);
  }

  const handleSetMaterial = (material) => {
    setMaterial(material);
    updateFRATcharts();
  }

  const handleSetWard = (ward) => {
    setWard(ward);
    updateFRATcharts();
  }

  const handleSetStartYear = (startYear) => {
    setStartYear(startYear);
    updateFRATcharts();
  }

  const handleSetEndYear = (endYear) => {
    setEndYear(endYear);
    updateFRATcharts();
  }

  useEffect(() => {
    updateFRATcharts();
  }, [material, ward, startYear, endYear])

  const handleSusceptibilityChart = (bacteria) => {
    setLoading(true);
    axios
      .get(`${BASE_URL}/chart/${bacteria}/${2000}/${dayjs().year()}`, {
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
        setChartData(modifySusceptibilityChartData(res.data));
      })
      .finally(() => {
        setLoading(false);
      });
  }

  const handleMaterialChart = (material, ward, startYear, endYear) => {
    setLoading(true);
    axios
      .get(`${BASE_URL}/chart/FRAT/${ward}/${material}/${startYear}/${endYear}`, {
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
        setMaterialChartData(modifySusceptibilityChartData(res.data.barChart));
        setGraphChartData(modifySusceptibilityChartData(res.data.graphChart));
      })
      .finally(() => {
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

    console.log(resultJson);

    return resultList;
  }

  return (
    <Box sx={{ width: '100%', height: '500px' }}>
      <Box sx={{ marginLeft: '24px', marginRight: '24px' }}>
        <Paper sx={{ padding: '10px' }}>
          <Tabs value={value} onChange={handleChange} aria-label="basic tabs" centered>
            <Tab label="Lekowrażliwość drobnoustrojów" {...a11yProps(0)} />
            <Tab label="FRAT" {...a11yProps(1)} />
            {/* <Tab label="DDD/1000 osobodni hospitalizacji" {...a11yProps(2)} /> */}
          </Tabs>
        </Paper>
      </Box>
      <TabPanel value={value} index={0}>
        <Grid container direction="row" spacing={2} wrap="nowrap">
        <Grid item xs={3}>
            <Grid container direction="column" spacing={3} wrap="nowrap">
              <Grid item>
                <BacteriaSelect handleSusceptibilityChart={handleSusceptibilityChart} />
              </Grid>
            </Grid>
          </Grid>
          <Grid item xs={9}>
            <SusceptibilityChart chartData={chartData} />
          </Grid>
        </Grid>
      </TabPanel>
      <TabPanel value={value} index={1}>
        <Grid container direction="column" spacing={2} wrap="nowrap">
        <Grid item xs={3}>
            <Grid container direction="row" spacing={2} wrap="nowrap">
              <Grid item xs={3}>
                <MaterialSelect handleSetMaterial={handleSetMaterial} />
              </Grid>
              <Grid item xs={3}>
                <WardSelect handleSetWard={handleSetWard} />
              </Grid>
              <Grid item xs={3}>
                <YearSelect handleSetYear={handleSetStartYear} label={"Data początkowa"} />
              </Grid>
              <Grid item xs={3}>
                <YearSelect handleSetYear={handleSetEndYear} label={"Data końcowa"} />
              </Grid>
            </Grid>
          </Grid>
          <Grid item xs={9}>
            <SusceptibilityChart chartData={materialChartData} />
          </Grid>
          <Grid item xs={9}>
            <RecommendationChart graphData={graphChartData} />
          </Grid>
        </Grid>
        
      </TabPanel>
      {/* <TabPanel value={value} index={2}>
        <Grid container direction="row" spacing={2} wrap="nowrap">
          <Grid item xs={9}>
            <RecommendationChart materialChartData={materialChartData} />
          </Grid>
          <Grid item xs={3}>
            <Grid container direction="column" spacing={3} wrap="nowrap">
              <Grid item>
                <MaterialSelect handleSetMaterial={handleSetMaterial} />
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      </TabPanel> */}
    </Box>
  );
}
