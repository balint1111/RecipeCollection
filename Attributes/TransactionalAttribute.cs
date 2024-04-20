using System.Transactions;
using Microsoft.AspNetCore.Mvc.Filters;

namespace EFGetStarted.Attributes;

public class TransactionalAttribute : Attribute, IAsyncActionFilter
{
    public async Task OnActionExecutionAsync(ActionExecutingContext context, ActionExecutionDelegate next)
    {
        using (var transactionScope = new TransactionScope(TransactionScopeAsyncFlowOption.Enabled))
        {
            ActionExecutedContext actionExecutedContext = await next();
            //if no exception were thrown
            if (actionExecutedContext.Exception == null)
                transactionScope.Complete();
                
        }
    }
}