import { render, screen } from '@testing-library/react';
import renderer from 'react-test-renderer';
import { Provider } from 'react-redux';
import { MemoryRouter } from 'react-router-dom';
import App from '../App';
import store from '../state/store';

test('renders application header', () => {
  const component = renderer.create(
  <MemoryRouter>
      <Provider store={store}>
        <App />
      </Provider>
    </MemoryRouter>);
  let tree = component.toJSON();
  expect(tree).toMatchSnapshot();
});
