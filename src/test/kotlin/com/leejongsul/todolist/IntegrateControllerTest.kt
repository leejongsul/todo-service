package com.leejongsul.todolist

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.leejongsul.todolist.dto.*
import io.kotest.core.spec.style.DescribeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class IntegrateControllerTest(
    @Autowired val mockMvc: MockMvc
) : DescribeSpec({
    describe("회원 가입") {
        val request = SignUpRequest("테스터1", "내비밀번호")
        val jsonBody = jacksonObjectMapper().writeValueAsBytes(request)
        val uri = "/api/users/signUp"

        context("닉네임을 사용할 수 있을 때 회원 가입을 하면") {
            it("201 CREATED") {
                mockMvc.perform(
                    post(uri)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated)
            }
        }
        context("닉네임을 사용할 수 없을 때 회원 가입을 하면") {
            it("409 CONFLICT 응답") {
                mockMvc.perform(
                    post(uri)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isConflict)
            }
        }
    }

    describe("할 일") {
        val responseBody = mockMvc.perform(
            post("/api/auth")
                .content(jacksonObjectMapper().writeValueAsBytes(AuthenticationRequest("테스터1", "내비밀번호")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().response.contentAsString

        val accessToken = jacksonObjectMapper().readValue<AuthenticationResponse>(responseBody).accessToken

        context("회원의 할 일이 없을 때 가장 최근 할 일을 가져오면") {
            it("404 Not Found") {
                mockMvc.perform(
                    get("/api/todo/latest")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound)
            }
        }

        context("회원의 할 일이 없을 때 할 일 목록을 가져오면") {
            it("200 OK") {
                mockMvc.perform(
                    get("/api/todo/list")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
                    .andExpect(jsonPath("$").isArray)
                    .andExpect(jsonPath("$").isEmpty)
                    .andReturn().response.contentAsString
            }
        }

        context("생성하면") {
            it("201 CREATED") {
                mockMvc.perform(
                    post("/api/todo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .content(jacksonObjectMapper().writeValueAsBytes(CreateTodoRequest("할 일 1", "할 일")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated)
            }
        }

        context("가장 최근 할 일을 가져오면") {
            it("200 OK") {
                mockMvc.perform(
                    get("/api/todo/latest")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
                    .andExpect(jsonPath("$.id").value(1))
            }
        }

        context("또 생성하면") {
            it("201 CREATED") {
                mockMvc.perform(
                    post("/api/todo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .content(jacksonObjectMapper().writeValueAsBytes(CreateTodoRequest("할 일 2", "할 일")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated)
            }
        }

        context("가장 최근 할 일을 가져오면") {
            it("200 OK") {
                mockMvc.perform(
                    get("/api/todo/latest")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
                    .andExpect(jsonPath("$.id").value(2))
            }
        }

        context("할 일 목록을 가져오면") {
            it("200 OK") {
                mockMvc.perform(
                    get("/api/todo/list")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
                    .andExpect(jsonPath("$.length()").value(2))
            }
        }

        context("상태를 [할 일] -> [진행 중]으로 수정했을 때") {
            it("200 OK") {
                mockMvc.perform(
                    put("/api/todo/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .content(jacksonObjectMapper().writeValueAsBytes(UpdateTodoStatusRequest("진행 중")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
            }
        }
        context("상태를 [진행 중] -> [대기]로 수정했을 때") {
            it("200 OK") {
                mockMvc.perform(
                    put("/api/todo/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .content(jacksonObjectMapper().writeValueAsBytes(UpdateTodoStatusRequest("대기")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
            }
        }
        context("상태를 [대기] -> [할 일]로 수정했을 때") {
            it("200 OK") {
                mockMvc.perform(
                    put("/api/todo/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .content(jacksonObjectMapper().writeValueAsBytes(UpdateTodoStatusRequest("할 일")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
            }
        }
        context("상태를 [할 일] -> [대기]로 수정했을 때") {
            it("400 Bad Request") {
                mockMvc.perform(
                    put("/api/todo/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .content(jacksonObjectMapper().writeValueAsBytes(UpdateTodoStatusRequest("대기")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest)
            }
        }
    }

    describe("회원 탈퇴") {
        val responseBody = mockMvc.perform(
            post("/api/auth")
                .content(jacksonObjectMapper().writeValueAsBytes(AuthenticationRequest("테스터1", "내비밀번호")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().response.contentAsByteArray

        val accessToken = jacksonObjectMapper().readValue<AuthenticationResponse>(responseBody).accessToken

        context("Authorization 헤더가 없을 때") {
            it("401 UNAUTHORIZED 응답") {
                mockMvc.perform(
                    delete("/api/users/withdraw")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnauthorized)
            }
        }
        context("Authorization 헤더가 있을 때") {
            it("200 OK 응답") {
                mockMvc.perform(
                    delete("/api/users/withdraw")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk)
            }
        }
    }
})