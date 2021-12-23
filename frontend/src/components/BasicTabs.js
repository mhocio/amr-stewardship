import * as React from 'react';
import PropTypes from 'prop-types';

// material-ui
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

// project imports
import SusceptibilityChart from './Charts/SusceptibilityChart';
import RecommendationChart from './Charts/RecommendationChart';
import DosageChart from './Charts/DosageChart';


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
    <Box sx={{ width: '100%', height: '500px'}}>
      <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Tabs value={value} onChange={handleChange} aria-label="basic tabs" centered>
          <Tab label="Zmiany rekomendacji grup antybiotyków" {...a11yProps(0)} />
          <Tab label="DDD/1000 osobodni hospitalizacji" {...a11yProps(1)} />
          <Tab label="Lekowrażliwość drobnoustrojów" {...a11yProps(2)} />
        </Tabs>
      </Box>
      <TabPanel value={value} index={0}>
        <RecommendationChart />
      </TabPanel>
      <TabPanel value={value} index={1}>
        <SusceptibilityChart />
      </TabPanel>
      <TabPanel value={value} index={2}>
        <DosageChart />
      </TabPanel>
    </Box>
  );
}
