package com.tick.gateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtUtilityTests {

    /* payload
        {
          "sub": "1234567890",
          "name": "John Doe",
          "iat": 1668590409
        }
     */
    private static final String TEST_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6ImNkNWFkNWIzY2FmZTgxYjhhZTMwMTgwODUyODY3NDhlIn0.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNjY4NTkwNDA5fQ.O4JGnI5GyKzx8aQ6LXC43xXP_kGPjb5ff0aG902SUwvReoBqNUp4Q1sTw9BasV7eDCEDpoGit8GUW-caQfzQO1xd_3n46JeQvU3GUgDhFfC9N7fXY1cD0NAf_rarhNtzQXOZHbtjMg0K1ZOPa76EH9t2BTXlauWclEZk10zWwUaeIsMAe6pUnEPD6UjzACAsyserbuPajGlbTPmPJMVLTwdIGHBNNrQmE5TRU0exgHOPXnxD3i1d4pRacIg6vSwF_PG8b0GeEMfuVMVeodMYSOB9lQOQXuXjUXV3AJ4X5ilQaIujP7PcTWU7apxlZPbjs0C_rH8eCHjdNwMFPVqPeYDNn8cMHeGsUuN7xuJCWDZC1kp-TZ79YT5V3hlVkTN8Arm0kr55XGPQQWAmhgXJQvBroVYo4kH_uiiThqXD9xU0rmqr0H6g2cmnaRtBRpB2eOEKFHY-CBhGBEQQ4nRVftcABAaEali-3kRd4exM05oujUop2F5bjwbdDLwBIMcR\n";

    @Test
    public void getClaims_WithTokenContainingClaims_ReturnsAllClaims() {

        // Arrange

        // Act
        var result = JwtUtility.getClaims(TEST_JWT);

        // Assert
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(3, result.size());

    }

    @Test
    public void getClaims_WithInvalidToken_ReturnsEmptyMap() {

        // Arrange
        final String testJwt = "bad token";

        // Act
        var result = JwtUtility.getClaims(testJwt);

        // Assert
        Assertions.assertTrue(result.isEmpty());

    }

}
