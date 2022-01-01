import PropTypes from 'prop-types';
import React from "react";

// material-ui
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid'
import Paper from '@mui/material/Paper';


// project imports
import SusceptibilityChart from '../Charts/SusceptibilityChart';
import RecommendationChart from '../Charts/RecommendationChart';
import DosageChart from '../Charts/DosageChart';
import AntibioticGroupSelect from '../AntibioticGroupSelect';
import AntibioticSelect from '../AntibioticSelect';
import YearSelect from '../YearSelect';

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

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <Box sx={{ width: '100%', height: '500px' }}>
      <Box sx={{ marginLeft: '24px', marginRight: '24px'}}>
        <Paper sx={{ padding: '20px' }}>
          <Tabs value={value} onChange={handleChange} aria-label="basic tabs" centered>
            <Tab label="Zmiany rekomendacji grup antybiotyków" {...a11yProps(0)} />
            <Tab label="Lekowrażliwość drobnoustrojów" {...a11yProps(1)} />
            <Tab label="DDD/1000 osobodni hospitalizacji" {...a11yProps(2)} />
          </Tabs>
        </Paper>
      </Box>
      <TabPanel value={value} index={0}>
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
      <TabPanel value={value} index={1}>
        <Grid container direction="row" spacing={2} wrap="nowrap">
          <Grid container direction="column" spacing={3} wrap="nowrap">
            <Grid item xs={9}>
              <SusceptibilityChart />
            </Grid>
          </Grid>
          <Grid item xs={3}>
            <Grid item>
              <AntibioticSelect />
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
      </TabPanel>
    </Box>
  );
}
