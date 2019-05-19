package parser;

public enum TransitionStates {
    program_0(0, 0), program_1(1, 0), program_2(2, 0),
    declaration_list_0(3, 0), declaration_list_1(4, 0), declaration_list_2(5, 0),
    declaration_0(6, 0), declaration_1(7, 0), declaration_2(8, 0),
    s1_0(9, 0), s1_1(10, 0), s1_2(11, 0),
    s10_0(12, 0), s10_1(13, 0), s10_2(14, 0), s10_3(15, 0), s10_4(16, 0), s10_5(17, 0), s10_6(18, 0), s10_7(19, 0),
    type_specifier_0(20, 0), type_specifier_1(21, 0),
    params_0(22, 0), params_1(23, 0), params_2(24, 0), params_3(25, 0), params_4(26, 0), params_5(27, 0),
    s11_0(28, 0), s11_1(29, 0), s11_2(30, 0), s11_3(31, 0),
    s2_0(32, 0), s2_1(33, 0), s2_2(34, 0), s2_3(35, 0),
    param_0(36, 0), param_1(37, 0), param_2(38, 0), param_3(39, 0),
    s3_0(40, 0), s3_1(41, 0), s3_2(42, 0),
    compound_stmt_0(43, 0), compound_stmt_1(44, 0), compound_stmt_2(45, 0), compound_stmt_3(46, 0), compound_stmt_4(47, 0),
    statement_list_0(48, 0), statement_list_1(49, 0), statement_list_2(50, 0),
    statement_0(51, 0), statement_1(52, 0),
    expression_stmt_0(53, 0), expression_stmt_1(54, 0), expression_stmt_2(55, 0),
    selection_stmt_0(56, 0), selection_stmt_1(57, 0), selection_stmt_2(58, 0), selection_stmt_3(59, 0), selection_stmt_4(60, 0), selection_stmt_5(61, 0), selection_stmt_6(62, 0), selection_stmt_7(63, 0),
    iteration_stmt_0(64, 0), iteration_stmt_1(65, 0), iteration_stmt_2(66, 0), iteration_stmt_3(67, 0), iteration_stmt_4(68, 0), iteration_stmt_5(69, 0),
    return_stmt_0(70, 0), return_stmt_1(71, 0), return_stmt_2(72, 0), return_stmt_3(73, 0), return_stmt_4(74, 0),
    switch_stmt_0(75, 0), switch_stmt_1(76, 0), switch_stmt_2(77, 0), switch_stmt_3(78, 0), switch_stmt_4(79, 0), switch_stmt_5(80, 0), switch_stmt_6(81, 0), switch_stmt_7(82, 0), switch_stmt_8(83, 0),
    case_stmts_0(84, 0), case_stmts_1(85, 0), case_stmts_2(86, 0),
    case_stmt_0(87, 0), case_stmt_1(88, 0), case_stmt_2(89, 0), case_stmt_3(90, 0), case_stmt_4(91, 0),
    default_stmt_0(92, 0), default_stmt_1(93, 0), default_stmt_2(94, 0), default_stmt_3(95, 0),
    expression_0(96, 0), expression_1(97, 0), expression_2(98, 0), expression_3(99, 0), expression_4(100, 0), expression_5(101, 0), expression_6(102, 0), expression_7(103, 0), expression_8(104, 0), expression_9(105, 0),
    s12_0(106, 0), s12_1(107, 0), s12_2(108, 0), s12_3(109, 0), s12_4(110, 0), s12_5(111, 0), s12_6(112, 0), s12_7(113, 0), s12_8(173, 0), s12_9(174, 0), s12_10(175, 0), s12_11(176, 0), s12_12(177, 0),
    s13_0(114, 0), s13_1(115, 0), s13_2(116, 0), s13_3(117, 0), s13_4(118, 0), s13_5(119, 0), s13_6(120, 0), s13_7(121, 0),
    var_0(122, 0), var_1(123, 0), var_2(124, 0),
    s5_0(125, 0), s5_1(126, 0), s5_2(127, 0), s5_3(128, 0),
    s6_0(129, 0), s6_1(130, 0), s6_2(131, 0),
    relop_0(132, 0), relop_1(133, 0), relop_2(134, 0),
    additive_expression_0(135, 0), additive_expression_1(136, 0), additive_expression_2(137, 0),
    s7_0(138, 0), s7_1(139, 0), s7_2(140, 0), s7_3(141, 0),
    addop_0(142, 0), addop_1(143, 0),
    term_0(144, 0), term_1(145, 0), term_2(146, 0),
    s8_0(147, 0), s8_1(148, 0), s8_2(149, 0), s8_3(150, 0),
    signed_factor_0(151, 0), signed_factor_1(152, 0), signed_factor_2(153, 0), signed_factor_3(154, 0),
    factor_0(155, 0), factor_1(156, 0), factor_2(157, 0), factor_3(158, 0), factor_4(178, 0),
    call_0(159, 0), call_1(160, 0), call_2(161, 0), call_3(162, 0), call_4(163, 0),
    args_0(164, 0), args_1(165, 0),
    arg_list_0(166, 0), arg_list_1(167, 0), arg_list_2(168, 0),
    s9_0(169, 0), s9_1(170, 0), s9_2(171, 0), s9_3(172, 0),
    s15_0(179, 0), s15_1(180, 0), s15_2(181, 0), s15_3(182, 0),
    s16_0(183, 0), s16_1(184, 0), s16_2(185, 0);

    int number;
    int lineNumber;

    TransitionStates(int number, int lineNumber) {
        this.number = number;
        this.lineNumber = lineNumber;
    }
}
