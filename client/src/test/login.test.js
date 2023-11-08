import { render, screen, cleanup, fireEvent } from "@testing-library/react";
import { useAuth0 } from "@auth0/auth0-react";
// if you're using jest 27.4.0+ and ts-jest 28.0.0+
import {expect, jest, test} from '@jest/globals';

import LoginComponent from "../components/application/loginComponent";
import { MemoryRouter } from "react-router-dom";
import { Provider } from "react-redux";
import store from "../state/store";


// if you're using ts-jest < 28.0.0
// import { mocked } from "ts-jest/utils";

const user = {
   email: "johndoe@me.com",
   email_verified: true,
   sub: "google-oauth2|12345678901234",
};

jest.mock("@auth0/auth0-react");

const mockedUseAuth0 = jest.mocked(useAuth0, true);

describe("LoginComponent Tests - Logged in", () => {
   beforeEach(() => {
       mockedUseAuth0.mockReturnValue({
           isAuthenticated: true,
           user,
           logout: jest.fn(),
           loginWithRedirect: jest.fn(),
           getAccessTokenWithPopup: jest.fn(),
           getAccessTokenSilently: jest.fn(),
           getIdTokenClaims: jest.fn(),
           loginWithPopup: jest.fn(),
           isLoading: false,
       });
   });
   afterEach(cleanup);
   test("Logout Button displays when logged in", () => {
       render(
        <MemoryRouter><Provider store={store}><LoginComponent /></Provider></MemoryRouter>    
       );
       const logoutButton = screen.getByText(/Logout/i);
       expect(logoutButton).toBeInTheDocument();
   });
   test("Click on logout runs logout action", () => {
        render(
            <MemoryRouter><Provider store={store}><LoginComponent /></Provider></MemoryRouter>
       );
       const logoutButton = screen.getByText(/Logout/i);
       fireEvent.click(logoutButton);
       const {logout} = useAuth0();
       expect(logout.mock.calls).toHaveLength(1); 
   });
});

describe("Test user not logged in", () => {
   beforeEach(() => {
       mockedUseAuth0.mockReturnValue({
           isAuthenticated: false,
           user: {},
           logout: jest.fn(),
           loginWithRedirect: jest.fn(),
           getAccessTokenWithPopup: jest.fn(),
           getAccessTokenSilently: jest.fn(),
           getIdTokenClaims: jest.fn(),
           loginWithPopup: jest.fn(),
           isLoading: false,
       });
   });
   afterEach(cleanup);
   test("Login button shown", () => {
       render(
        <MemoryRouter><Provider store={store}><LoginComponent /></Provider></MemoryRouter>
       );
       const loginButton = screen.getByText(/Login/i);
       expect(loginButton).toBeInTheDocument();
   });
   test("Called getAccessTokenWithPopup when login button clicked", () => {
    render(
        <MemoryRouter><Provider store={store}><LoginComponent /></Provider></MemoryRouter>
    );
    const loginButton = screen.getByText(/Login/i);
    fireEvent.click(loginButton);
    const {loginWithRedirect} = useAuth0();
    expect(loginWithRedirect.mock.calls).toHaveLength(1);
    });   
});