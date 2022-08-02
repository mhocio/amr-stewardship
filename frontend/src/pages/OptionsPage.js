import React, { useState, useEffect, useRef } from "react";

import BASE_URL from "../constants/BASE_URL";
import { useLoading } from "../loading/loading-context";
import axios from "../services/interceptor";
import authHeader from "../services/auth-header";
import ReactJson from 'react-json-view';

import { DrawerHeader } from "../styledComponents/StyledDrawerHeader";
import { Button } from "@mui/material";

import { Formik, Form, Field } from "formik";

const OptionsPage = () => {

    return (
        <>
            <DrawerHeader />
            <Formik
                initialValues={{
                    checked: false,
                    checked2: false,
                    checked3: false
                }}
                onSubmit={async (values, { resetForm }) => {
                    if (values.checked == true && values.checked2 == true && values.checked3 == true) {
                        axios
                            .delete(`${BASE_URL}/antibiogram/data`, {
                                method: "DELETE",
                                mode: "cors",
                                headers: {
                                    Accept: "application/json, text/plain",
                                    "Content-Type": "application/json",
                                    "Access-Control-Allow-Origin": "*",
                                    ...authHeader()
                                }
                            })
                            .then((res) => {
                                console.log(res);
                                if (res.status == 200)
                                    alert("USUNIĘTO DANE");
                            })
                            .catch((error) => {
                            })
                            .finally(() => {
                            });
                    } else {
                        alert("zaznacz pola usun i kliknij 'Usun baze danych' aby usunac ");
                    }

                    resetForm();
                }}>

                <Form>
                    <div role="group" aria-labelledby="checkbox-group">
                        <label>
                            <Field type="checkbox" name="checked" />
                            usun
                        </label>
                        <label>
                            <Field type="checkbox" name="checked2" />
                            usun
                        </label>
                        <label>
                            <Field type="checkbox" name="checked3" />
                            usun
                        </label>
                    </div>
                    <Button type="submit">
                        Usun baze danych
                    </Button>
                </Form>
            </Formik>
        </>

    );

}

export default OptionsPage;