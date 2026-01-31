package com.example.recipecollection.middleware

import com.example.recipecollection.dto.MiddlewareReturnDto
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import tools.jackson.databind.ObjectMapper
import java.util.UUID

@Component
class RequestResponseMiddleware(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val identity = UUID.randomUUID().toString()
        request.setAttribute(REQUEST_ID_ATTRIBUTE, identity)
        val wrappedResponse = ContentCachingResponseWrapper(response)
        try {
            filterChain.doFilter(request, wrappedResponse)
            val originalBody = wrappedResponse.contentAsByteArray
            val content: Any? = extractContent(originalBody)
            val middlewareBody = MiddlewareReturnDto(
                statusCode = wrappedResponse.status.toString(),
                content = content,
                identity = identity,
            )
            writeResponse(wrappedResponse, middlewareBody)
        } catch (ex: Exception) {
            ex.printStackTrace()
            val middlewareBody = MiddlewareReturnDto(
                statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR.toString(),
                content = ex.message ?: "Unexpected error",
                identity = identity,
            )
            response.resetBuffer()
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = Charsets.UTF_8.name()
            response.writer.write(objectMapper.writeValueAsString(middlewareBody))
            response.flushBuffer()
            return
        }
        wrappedResponse.copyBodyToResponse()
    }

    private fun extractContent(body: ByteArray): Any? {
        if (body.isEmpty()) {
            return null
        }
        return try {
            objectMapper.readValue(body, Any::class.java)
        } catch (ex: Exception) {
            body.toString(Charsets.UTF_8)
        }
    }

    private fun writeResponse(
        response: ContentCachingResponseWrapper,
        body: MiddlewareReturnDto,
    ) {
        response.resetBuffer()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = Charsets.UTF_8.name()
        response.writer.write(objectMapper.writeValueAsString(body))
        response.flushBuffer()
    }

    companion object {
        const val REQUEST_ID_ATTRIBUTE = "ID"
    }
}
