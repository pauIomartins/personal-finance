module.exports = {
  testEnvironment: 'jsdom',
  roots: ['<rootDir>/src/test/javascript'],
  testMatch: ['**/*.test.js'],
  verbose: true,
  collectCoverage: true,
  collectCoverageFrom: [
    'src/main/resources/static/js/**/*.js'
  ],
  coverageDirectory: 'target/jest-coverage',
  coverageReporters: ['text', 'lcov', 'html']
};
