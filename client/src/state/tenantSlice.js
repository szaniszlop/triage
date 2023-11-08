import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'

const initialState = {
  data: [],
  status: "pending",
  error: "",
  info: ""
}

export const fetchTenants = createAsyncThunk('tenants/fetchTenants', async (client) => {
  console.log("fetchTenants", client)
  const response = await client.getTenants()
  console.log("fetchTenants", response)
  return response
})

const tenantSlice = createSlice({
  name: 'tenants',
  initialState,
  reducers: {
    tenantUpdated( state, action){
        console.log("tenantUpdated", action);
        state.data.map((tenant) => {return tenant.id === action.payload.id ? action.payload : tenant});
    },
    tenantDeleted( state, action){
        console.log("tenantDeleted", action);
        state.data.filter((tenant) => {return tenant.id !== action.payload.id });
    },
    resetStatus(state, action){
      state.status = "pending"
      state.error = ""
    }
  },
  extraReducers(builder) {
    builder
    .addCase(fetchTenants.pending, (state, action) =>{
      console.log("fetchTenants pending");
      state.status = "loading";
    })
    .addCase(fetchTenants.fulfilled, (state, action) => {
      console.log("fetchTenants loaded");
      state.status = "loaded"
      state.data = action.payload;
    })
    .addCase(fetchTenants.rejected, (state, action) => {
      console.log("fetchTenants failed");
      state.status = "failed"
      state.error = action.error.message
      state.data = []
    })
  }
})

export const { tenantUpdated,  tenantDeleted, resetStatus} = tenantSlice.actions

export const selectTenants = state => state.tenants.data;
export const selectTenantsStatus = state => state.tenants.status;
export const selectTenantsError = state => state.tenants.error;
export const selectTenantsInfo = state => state.tenants.info;
export const selectTenant = (id) => (state) => state.tenants.data.find( tenant => tenant.id === id);

export default tenantSlice.reducer
