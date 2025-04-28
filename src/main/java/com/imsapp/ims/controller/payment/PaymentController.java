package com.imsapp.ims.controller.payment;

import com.imsapp.ims.entity.payment.Payment;
import com.imsapp.ims.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ims/payments")
public class PaymentController {


    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private PaymentService paymentService;

    @Operation(summary = "Pay premium for a policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })

    @PostMapping("/premium")
    public ResponseEntity<Payment> payPremium(@RequestParam String policyNumber,
                                              @RequestParam Double amount) {
        Payment payment = paymentService.payPremium(policyNumber, amount);
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "Get payments by policy number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)) }),
            @ApiResponse(responseCode = "404", description = "Policy not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/policy/{policyNumber}")
    public ResponseEntity<List<Payment>> getPaymentsByPolicy(@PathVariable String policyNumber) {
        List<Payment> payments = paymentService.getPaymentsByPolicy(policyNumber);
        return ResponseEntity.ok(payments);
    }
    @Operation(summary = "Update payment amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestParam @Valid Double newAmount) {
        Payment updatedPayment = paymentService.updatePayment(id, newAmount);
        return ResponseEntity.ok(updatedPayment);
    }

    @Operation(summary = "Get total paid amount for a policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total paid amount retrieved successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class)) }),
            @ApiResponse(responseCode = "404", description = "Policy not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/policy/{policyNumber}/total-paid")
    public ResponseEntity<Double> getTotalPaidAmount(@PathVariable String policyNumber) {
        Double totalPaid = paymentService.getTotalPaidAmount(policyNumber);
        return ResponseEntity.ok(totalPaid);
    }
    @Operation(summary = "Check balance premium for a policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance premium retrieved successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class)) }),
            @ApiResponse(responseCode = "404", description = "Policy not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/policy/{policyNumber}/balance")
    public ResponseEntity<Double> checkBalancePremium(@PathVariable String policyNumber) {
        Double balancePremium = paymentService.checkBalancePremium(policyNumber);
        return ResponseEntity.ok(balancePremium);
    }
    @Operation(summary = "Check if policy is fully paid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Policy payment status retrieved successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)) }),
            @ApiResponse(responseCode = "404", description = "Policy not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })

    @GetMapping("/policy/{policyNumber}/is-fully-paid")
    public ResponseEntity<Boolean> isPolicyFullyPaid(@PathVariable String policyNumber) {
        boolean isFullyPaid = paymentService.isPolicyFullyPaid(policyNumber);
        return ResponseEntity.ok(isFullyPaid);
    }


}

