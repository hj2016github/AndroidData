final RadioGroup radioGroup = view.findViewById(R.id.pop_alarm_rg);
        final int len = radioGroup.getChildCount();//获得单选按钮组的选线个数

         radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < len ; i++) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    if (radioButton.isClickable()){
                        ColorStateList colorStateList =  context.getResources().getColorStateList(R.color.btntxtselector);
                        radioButton.setTextColor(colorStateList);
                    }
                    radioButton.setTextColor(context.getResources().getColor(R.color.hintColor));
                }